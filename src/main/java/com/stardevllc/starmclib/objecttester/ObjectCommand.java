package com.stardevllc.starmclib.objecttester;

import com.stardevllc.starlib.reflection.ReflectionHelper;
import com.stardevllc.starmclib.color.ColorUtils;
import com.stardevllc.starmclib.objecttester.codex.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * This is a dev based utility that allows seeing and modifying objects directly using reflection. <br>
 * To use this, instantiate this class and then register the command yourself. 
 */
@SuppressWarnings("ExtractMethodRecommender")
public class ObjectCommand<T> implements TabExecutor {

    protected static final Set<TypeCodex> DEFAULT_CODECS = new HashSet<>();

    static {
        DEFAULT_CODECS.addAll(List.of(new BooleanCodex(), new ByteCodex(), new BooleanCodex(), new CharCodex(), new DoubleCodex(), new FloatCodex(), new IntegerCodex(),
                new LongCodex(), new ShortCodex(), new StringCodex(), new UUIDCodex()));

    }

    private JavaPlugin plugin;
    private Class<T> baseClass;
    private String permission;

    private Map<String, Selector<T>> selectors = new HashMap<>();
    private Set<TypeCodex> codecs = new HashSet<>();

    private Field[] fields;
    private Method[] methods;
    
    private Map<String, T> selectedObjects = new HashMap<>();
    
    public ObjectCommand(JavaPlugin plugin, Class<T> baseClass, String permission) {
        this.plugin = plugin;
        this.baseClass = baseClass;
        this.permission = permission;

        Set<Field> classFields = ReflectionHelper.getClassFields(baseClass);
        fields = new Field[classFields.size()];
        Comparator<Field> fieldComparator = Comparator.comparing(Field::getName);
        Set<Field> constants = new TreeSet<>(fieldComparator), staticFields = new TreeSet<>(fieldComparator),
                instanceFinal = new TreeSet<>(fieldComparator), instance = new TreeSet<>(fieldComparator);
        for (Field classField : classFields) {
            int modifiers = classField.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            boolean isFinal = Modifier.isFinal(modifiers);
            if (isStatic && isFinal) {
                constants.add(classField);
            } else if (isStatic) {
                staticFields.add(classField);
            } else if (isFinal) {
                instanceFinal.add(classField);
            } else {
                instance.add(classField);
            }
        }

        AtomicInteger index = new AtomicInteger(0);
        addFields(constants, index);
        addFields(staticFields, index);
        addFields(instanceFinal, index);
        addFields(instance, index);

        Set<Method> classMethods = ReflectionHelper.getClassMethods(baseClass);
        methods = new Method[classMethods.size()];
        Comparator<Method> methodComparator = Comparator.comparing(Method::getName).thenComparingInt(Method::getParameterCount);
        Set<Method> staticMethods = new TreeSet<>(methodComparator), instanceMethods = new TreeSet<>(methodComparator);
        for (Method classMethod : classMethods) {
            int modifiers = classMethod.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            if (isStatic) {
                staticMethods.add(classMethod);
            } else {
                instanceMethods.add(classMethod);
            }
        }

        index.set(0);
        addMethods(staticMethods, index);
        addMethods(instanceMethods, index);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ColorUtils.color("You do not have permission to use that command."));
            return true;
        }

        if (!(args.length > 0)) {
            sender.sendMessage(ColorUtils.color("&cYou do not have enough arguments."));
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (!(args.length > 1)) {
                ColorUtils.coloredMessage(sender, "&cYou must specify what to list.");
                return true;
            }

            List<String> lines = new LinkedList<>();
            String key = "", typeOverride = "";
            if (args[1].equalsIgnoreCase("fields")) {
                key = "&7Key: &aName &8- &cType? &8- &3Static? &8- &dModifiable?";
                for (Field field : fields) {
                    String name = field.getName();
                    String typeName = field.getType().getSimpleName();
                    int modifiers = field.getModifiers();
                    boolean isStatic = Modifier.isStatic(modifiers);
                    boolean isFinal = Modifier.isFinal(modifiers);
                    lines.add("  &8- &a" + name + " &8- &c" + typeName + " &8- &3" + isStatic + " &8- &d" + !isFinal);
                }
            } else if (args[1].equalsIgnoreCase("values")) {
                key = "&7&oYou will only see values if their type has a TypeCodex.\n&7&oYou will only see instance values if you have a selection.";
                typeOverride = "field values";
                T selectedObject = null;
                if (args.length > 2) {
                    selectedObject = this.selectedObjects.get(args[2]);
                }
                for (Field field : fields) {
                    String name = field.getName();
                    String typeName = field.getType().getSimpleName();
                    int modifiers = field.getModifiers();
                    boolean isStatic = Modifier.isStatic(modifiers);
                    boolean isFinal = Modifier.isFinal(modifiers);
                    Object value;
                    if (isStatic) {
                        try {
                            value = field.get(null);
                        } catch (IllegalAccessException e) {
                            return handleException(sender, "There was an error getting a static field", e);
                        }
                    } else {
                        if (selectedObject == null) {
                            continue;
                        }

                        try {
                            value = field.get(selectedObject);
                        } catch (IllegalAccessException e) {
                            return handleException(sender, "There was an error getting an instance field", e);
                        }
                    }

                    TypeCodex codex = getCodex(field.getType());
                    if (codex == null) {
                        continue;
                    }

                    String format = "&8- &3{static}&c{final}&e" + field.getName() + "&8: &a" + codex.serialize(value);
                    if (isStatic) {
                        format = format.replace("{static}", "static ");
                    } else {
                        format = format.replace("{static}", "");
                    }

                    if (isFinal) {
                        format = format.replace("{final}", "final ");
                    } else {
                        format = format.replace("{final}", "");
                    }

                    lines.add(format);
                }
            } else if (args[1].equalsIgnoreCase("methods")) {
                sender.sendMessage(ColorUtils.color("&7Key: &aName &8- &3Static? &8- &cReturn Type &8- &dParameter Count"));
                for (Method method : methods) {
                    String name = method.getName();
                    String returnType = method.getReturnType().getSimpleName();
                    int modifiers = method.getModifiers();
                    boolean isStatic = Modifier.isStatic(modifiers);
                    String format = "&8- &3{static}&e" + method.getName() + "&8: &a" + method.getParameters().length;
                    if (isStatic) {
                        format = format.replace("{static}", "static");
                    } else {
                        format = format.replace("{static}", "");
                    }

                    lines.add(format);
                }
            } else {
                return true;
            }

            String type = typeOverride.isEmpty() ? args[1].toLowerCase() : typeOverride;
            ColorUtils.coloredMessage(sender, "&6List of &a" + type + " &6in class &e" + baseClass.getSimpleName());
            ColorUtils.coloredMessage(sender, key);
            lines.forEach(line -> ColorUtils.coloredMessage(sender, line));
        } else if (args[0].equalsIgnoreCase("get")) {
            if (!(args.length > 1)) {
                sender.sendMessage(ColorUtils.color("/<cmd> get <fieldName> [object]"));
                return true;
            }
            
            T selectedObject = null;
            if (args.length > 2) {
                selectedObject = this.selectedObjects.get(args[2]);
            }

            for (Field field : this.fields) {
                if (field.getName().equalsIgnoreCase(args[1])) {
                    if (selectedObject == null && !Modifier.isStatic(field.getModifiers())) {
                        sender.sendMessage(ColorUtils.color("&cYou must select an object before you can get the field value."));
                        return true;
                    }

                    try {
                        sender.sendMessage(ColorUtils.color("&eValue for field &b" + field.getName() + " &eis &a" + field.get(selectedObject)));
                    } catch (IllegalAccessException e) {
                        return handleException(sender, "Error while getting field value", e);
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("select")) {
            if (!(args.length > 1)) {
                sender.sendMessage(ColorUtils.color("&cYou must use a sub command"));
                return true;
            }

            if (args[1].equalsIgnoreCase("new")) {
                if (!(args.length > 2)) {
                    ColorUtils.coloredMessage(sender, "&cYou must provide a name for this new selection.");
                    return true;
                }

                String variableName = args[2];
                
                Constructor<T> constructor;
                int argCount = args.length - 3;
                Object[] constructorArguments = new Object[argCount];

                if (args.length == 3) {
                    try {
                        constructor = baseClass.getDeclaredConstructor();
                    } catch (NoSuchMethodException e) {
                        return handleException(sender, "Could not get the no-args constructor", e);
                    }
                } else {
                    List<Constructor<?>> argCountFiltered = new ArrayList<>();
                    for (Constructor<?> c : baseClass.getDeclaredConstructors()) {
                        if (c.getParameters().length == argCount) {
                            argCountFiltered.add(c);
                        }
                    }

                    if (argCountFiltered.isEmpty()) {
                        ColorUtils.coloredMessage(sender, "&cThere was no constructors found with " + argCount + " arguments.");
                        ColorUtils.coloredMessage(sender, "&cVarargs are not supported at this time.");
                        return true;
                    }

                    Class<?>[] constructorParams = new Class[argCount];
                    for (int i = 3; i < args.length; i++) {
                        String arg = args[i];
                        int paramIndex = i - 1;
                        TypeCodex codex = null;
                        if (arg.contains(":")) {
                            String[] split = arg.split(":");
                            if (split.length == 2) {
                                for (TypeCodex codec : DEFAULT_CODECS) {
                                    if (codec.getOverridePrefix().equalsIgnoreCase(split[0])) {
                                        codex = codec;
                                        constructorParams[paramIndex] = codex.getMainClass();
                                    }
                                }
                            }
                        }

                        if (codex == null) {
                            paramIndex = i - 3;
                            plugin.getLogger().info(paramIndex + "");
                            for (Iterator<Constructor<?>> iterator = argCountFiltered.iterator(); iterator.hasNext(); ) {
                                Constructor<?> con = iterator.next();
                                Parameter[] params = con.getParameters();
                                codex = getCodex(params[paramIndex].getType());
                                if (codex == null) {
                                    iterator.remove();
                                } else {
                                    constructorParams[paramIndex] = params[paramIndex].getType();
                                }
                            }
                        }

                        try {
                            constructorArguments[paramIndex] = codex.deserialize(args[i]);
                        } catch (Exception e) {
                            return handleException(sender, "There was an error while parsing a parameter", e);
                        }
                    }
                    try {
                        constructor = baseClass.getDeclaredConstructor(constructorParams);
                    } catch (Exception e) {
                        return handleException(sender, "There was an error while getting the constructor", e);
                    }
                }

                constructor.setAccessible(true);
                try {
                    T selectedObject = constructor.newInstance(constructorArguments);
                    this.selectedObjects.put(variableName, selectedObject);
                    ColorUtils.coloredMessage(sender, "&aCreated a new object of type &e" + baseClass.getSimpleName() + " &a with the name &b" + variableName);
                } catch (Exception e) {
                    return handleException(sender, "There was an error while creating the instance", e);
                }
            } else if (args[1].equalsIgnoreCase("reset")) {
                if (!(args.length > 2)) {
                    ColorUtils.coloredMessage(sender, "&cYou must provide a variable name.");
                    return true;
                }
                T removed = this.selectedObjects.remove(args[2]);
                if (removed != null) {
                    sender.sendMessage(ColorUtils.color("&eThe object &b" + args[2] + " &ehas been removed."));
                } else {
                    ColorUtils.coloredMessage(sender, "&cThere was no object associated with that name.");
                }
            } else {
                if (!(args.length > 2)) {
                    ColorUtils.coloredMessage(sender, "&cYou must provide a name for that object.");
                    return true;
                }
                
                String variableName = args[2];
                int sal = args.length - 3;
                String[] selectorArgs = new String[sal];
                if (sal > 0) {
                    System.arraycopy(args, 3, selectorArgs, 0, sal);
                }

                for (Map.Entry<String, Selector<T>> entry : selectors.entrySet()) {
                    if (args[1].equalsIgnoreCase(entry.getKey())) {
                        T selectedObject = entry.getValue().select(sender, selectorArgs);
                        if (selectedObject == null) {
                            sender.sendMessage(ColorUtils.color("&cThe selector &e" + entry.getValue() + " &creturned a null value."));
                        } else {
                            this.selectedObjects.put(variableName, selectedObject);
                        }
                        break;
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!(args.length > 2)) {
                sender.sendMessage(ColorUtils.color("&cYou must provide a field and a new value."));
                return true;
            }
            
            T selectedObject = this.selectedObjects.get(args[1]);

            Field field = null;
            for (Field f : fields) {
                if (f.getName().equalsIgnoreCase(args[1])) {
                    field = f;
                    break;
                }
            }

            if (!Modifier.isStatic(field.getModifiers()) && selectedObject == null) {
                sender.sendMessage(ColorUtils.color("&cYou must have an instance selected to modify that field."));
                return true;
            }
            
            int startArgIndex = selectedObject != null ? 3 : 2;

            StringBuilder sb = new StringBuilder();
            for (int i = startArgIndex; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }

            String combinedArgs = sb.toString().trim();

            TypeCodex codex = null;
            for (TypeCodex c : DEFAULT_CODECS) {
                if (c.isValidType(field.getType())) {
                    codex = c;
                    break;
                }
            }

            if (codex == null) {
                sender.sendMessage(ColorUtils.color("&cThat field has a type that is not supported for setting values."));
                return true;
            }

            Object newValue;

            try {
                newValue = codex.deserialize(combinedArgs);
            } catch (Exception e) {
                sender.sendMessage(ColorUtils.color("&cThere was an error while parsing the value: " + e.getClass().getSimpleName()));
                sender.sendMessage(ColorUtils.color("&cPlease see console for more details."));
                return true;
            }

            if (newValue == null) {
                sender.sendMessage(ColorUtils.color("&cThe codex returned a null value."));
                sender.sendMessage(ColorUtils.color("&cThe default codexes return null if an invalid value is passed in."));
                sender.sendMessage(ColorUtils.color("&cSee the documentation of addons to see what they do for the deserialize method."));
                return true;
            }

            try {
                if (Modifier.isStatic(field.getModifiers())) {
                    field.set(null, newValue);
                } else {
                    field.set(selectedObject, newValue);
                }
                sender.sendMessage(ColorUtils.color("&aYou set the field &e" + field.getName() + " &ain the class &e" + baseClass.getSimpleName() + " &ato &b" + newValue));
            } catch (Exception e) {
                sender.sendMessage(ColorUtils.color("&cThere was an error setting the value to the field: " + e.getClass().getSimpleName()));
                sender.sendMessage(ColorUtils.color("&cSee console for more details."));
                plugin.getLogger().log(Level.SEVERE, "", e);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("call")) {
            if (!(args.length > 1)) {
                ColorUtils.coloredMessage(sender, "&cYou must provide a method name to call.");
                return true;
            }
            
            T selectedObject = this.selectedObjects.get(args[1]);
            String methodName = selectedObject != null ? args[2] : args[1];

            List<Method> filteredMethods = new ArrayList<>();
            int argCount = args.length - (selectedObject != null ? 3 : 2);
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase(methodName)) {
                    if (argCount == method.getParameterCount()) {
                        filteredMethods.add(method);
                    }
                }
            }

            if (filteredMethods.isEmpty()) {
                ColorUtils.coloredMessage(sender, "Could not find any methods with the name &e" + methodName + " &cand a parameter count of &e" + argCount);
                return true;
            }

            if (filteredMethods.size() == 1) {
                Method method = filteredMethods.get(0);
                Object[] params = new Object[argCount];
                for (int i = 0; i < method.getParameters().length; i++) {
                    Parameter parameter = method.getParameters()[i];
                    TypeCodex codex = getCodex(parameter.getType());
                    if (codex == null) {
                        ColorUtils.coloredMessage(sender, "&cInvalid type for a parameter on that method.");
                        return true;
                    }

                    try {
                        Object deserialized = codex.deserialize(args[i + (selectedObject != null ? 3 : 2)]);
                        params[i] = deserialized;
                    } catch (Exception e) {
                        ColorUtils.coloredMessage(sender, "&cCould not parse the argument at " + i + (selectedObject != null ? 3 : 2) + ": " + e.getClass().getSimpleName());
                        e.printStackTrace();
                        return true;
                    }
                }

                callMethod(method, params, sender, selectedObject);
                return true;
            }

            Object[] params = new Object[argCount];
            Iterator<Method> methodIterator = filteredMethods.iterator();
            methodLoop:
            while (methodIterator.hasNext()) {
                Method method = methodIterator.next();
                Parameter[] parameters = method.getParameters();
                for (int pi = 0; pi < parameters.length; pi++) {
                    int argIndex = pi + (selectedObject != null ? 3 : 2);

                    TypeCodex codex = getCodex(parameters[pi].getType());
                    if (codex == null) {
                        methodIterator.remove();
                        continue methodLoop;
                    }

                    try {
                        params[pi] = codex.deserialize(args[argIndex]);
                        if (params[pi] == null) {
                            methodIterator.remove();
                            continue methodLoop;
                        }
                    } catch (Exception e) {
                        methodIterator.remove();
                        continue methodLoop;
                    }
                }
            }

            if (filteredMethods.isEmpty()) {
                ColorUtils.coloredMessage(sender, "&cNo valid methods exist for the arguments provided.");
                return true;
            }

            if (filteredMethods.size() > 1) {
                ColorUtils.coloredMessage(sender, "&cThere are " + filteredMethods.size() + " possible methods, this really shouldn't be the case.");
                return true;
            }

            Method method = filteredMethods.get(0);
            callMethod(method, params, sender, selectedObject);
        }

        return true;
    }

    private void callMethod(Method method, Object[] params, CommandSender sender, T selectedObject) {
        Object returnObject;
        try {
            if (Modifier.isStatic(method.getModifiers())) {
                returnObject = method.invoke(null, params);
            } else {
                if (selectedObject == null) {
                    ColorUtils.coloredMessage(sender, "&cYou must select an object in order to call that method.");
                    return;
                }

                returnObject = method.invoke(selectedObject, params);
            }
        } catch (Exception e) {
            ColorUtils.coloredMessage(sender, "&cError while calling that method: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return;
        }

        Class<?> returnType = method.getReturnType();
        if (returnType != void.class) {
            TypeCodex returnCodex = getCodex(returnType);
            if (returnCodex == null) {
                ColorUtils.coloredMessage(sender, "&cNo TypeCodex was found for the return type: " + returnType.getSimpleName());
                return;
            }

            ColorUtils.coloredMessage(sender, "&aReturned Value from method call: &e" + returnCodex.serialize(returnObject));
        } else {
            ColorUtils.coloredMessage(sender, "&aCalled method &e" + method.getName() + " &asuccessfully. This method has no return value.");
        }
    }

    private TypeCodex getCodex(Class<?> type) {
        for (TypeCodex codec : DEFAULT_CODECS) {
            if (codec.isValidType(type)) {
                return codec;
            }
        }

        for (TypeCodex codec : codecs) {
            if (codec.isValidType(type)) {
                return codec;
            }
        }
        
        return null;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(permission)) {
            return new ArrayList<>();
        }

        if (args.length == 0) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(-1);
        if (args.length == 1) {
            completions.addAll(List.of("list", "get", "select", "set", "call"));
            index.set(0);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 2) {
                completions.addAll(List.of("fields", "values", "methods"));
                index.set(1);
            }
        } else if (args[0].equalsIgnoreCase("get")) {
            if (args.length == 2) {
                completions.addAll(getFieldNames());
                index.set(1);
            }
        } else if (args[0].equalsIgnoreCase("select")) {
            if (args.length == 2) {
                completions.addAll(List.of("new", "reset"));
                completions.addAll(selectors.keySet());
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length == 2) {
                completions.addAll(getFieldNames());
            }
        }

        if (index.get() > -1) {
            completions.removeIf(str -> !str.toLowerCase().startsWith(args[index.get()].toLowerCase()));
        }

        return completions;
    }

    public void addSelector(String arg, Selector<T> selector) {
        this.selectors.put(arg, selector);
    }
    
    public void addCodec(TypeCodex codex) {
        this.codecs.add(codex);
    }

    private List<String> getFieldNames() {
        List<String> names = new ArrayList<>();
        for (Field field : this.fields) {
            names.add(field.getName());
        }
        return names;
    }

    public void register() {
        PluginCommand cmd = plugin.getCommand(baseClass.getSimpleName().toLowerCase());
        if (cmd == null) {
            plugin.getLogger().severe("Could not register " + baseClass.getSimpleName() + " as an object command.");
            return;
        }
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private void addFields(Set<Field> fields, AtomicInteger index) {
        for (Field field : fields) {
            field.setAccessible(true);
            this.fields[index.get()] = field;
            index.getAndIncrement();
        }
    }

    private void addMethods(Set<Method> methods, AtomicInteger index) {
        for (Method method : methods) {
            try {
                method.setAccessible(true);
                this.methods[index.get()] = method;
                index.getAndIncrement();
            } catch (Exception e) {
                plugin.getLogger().warning("Could not add the method " + method.getName() + " from the class " + this.baseClass.getSimpleName());
            }
        }
    }

    private boolean handleException(CommandSender sender, String message, Exception e) {
        ColorUtils.coloredMessage(sender, "&c" + message + ": " + e.getClass().getSimpleName());
        ColorUtils.coloredMessage(sender, "&cSee console for more details.");
        plugin.getLogger().log(Level.SEVERE, "", e);
        return true;
    }
}
