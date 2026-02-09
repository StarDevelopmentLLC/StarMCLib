package com.stardevllc.starmclib.command.params;

import java.util.*;

@SuppressWarnings("DuplicatedCode")
public class CmdParams {
    protected final Set<Param> params = new HashSet<>();
    
    public CmdParams(Set<Param> params) {
        this.params.addAll(params);
    }
    
    public CmdParams(Param... params) {
        if (params != null) {
            this.params.addAll(Set.of(params));
        }
    }
    
    public void addFlag(Param param, Param... moreParams) {
        this.params.add(param);
        if (moreParams != null) {
            this.params.addAll(Set.of(moreParams));
        }
    }
    
    public ParamResult parse(String[] args) {
        LinkedList<String> argsList = new LinkedList<>(List.of(args));
        ListIterator<String> iterator = argsList.listIterator();
        
        Map<Param, Object> paramValues = new HashMap<>();
        
        while (iterator.hasNext()) {
            String arg = iterator.next();
            
            for (Param param : this.params) {
                if (arg.startsWith(param.id() + ":")) {
                    iterator.remove();
                    if (iterator.hasNext()) {
                        StringBuilder value = new StringBuilder(iterator.next());
                        iterator.remove();
                        if (!value.toString().startsWith("\"")) {
                            paramValues.put(param, value.toString());
                        } else {
                            while (iterator.hasNext()) {
                                String next = iterator.next();
                                if (next.endsWith("\"")) {
                                    value.append(" ").append(next);
                                    iterator.remove();
                                    break;
                                } else {
                                    value.append(" ").append(next);
                                    iterator.remove();
                                }
                            }
                            paramValues.put(param, value.toString().replace("\"", ""));
                        }
                    } else {
                        paramValues.put(param, param.defaultValue());
                    }
                    break;
                }
            }
        }
        
        for (Param flag : this.params) {
            if (!paramValues.containsKey(flag)) {
                paramValues.put(flag, flag.defaultValue());
            }
        }
        
        return new ParamResult(argsList.toArray(new String[0]), paramValues);
    }
}