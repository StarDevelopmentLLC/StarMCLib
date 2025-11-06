package com.stardevllc.starmclib.command.flags;

import java.util.*;

public class CmdFlags {
    protected final Set<Flag> flags = new HashSet<>();
    
    public CmdFlags(Set<Flag> flags) {
        this.flags.addAll(flags);
    }
    
    public CmdFlags(Flag... flags) {
        if (flags != null) {
            this.flags.addAll(Set.of(flags));
        }
    }
    
    public void addFlag(Flag flag, Flag... moreFlags) {
        this.flags.add(flag);
        if (moreFlags != null) {
            this.flags.addAll(Set.of(moreFlags));
        }
    }
    
    public FlagResult parse(String[] args) {
        LinkedList<String> argsList = new LinkedList<>(List.of(args));
        ListIterator<String> iterator = argsList.listIterator();
        
        Map<Flag, Object> flagValues = new HashMap<>();
        
        while (iterator.hasNext()) {
            String arg = iterator.next();

            for (Flag flag : this.flags) {
                if (arg.startsWith("-" + flag.id())) {
                    if (flag.type() == FlagType.PRESENCE) {
                        flagValues.put(flag, flag.valueIfPresent());
                        iterator.remove();
                        break;
                    } else if (flag.type() == FlagType.COMPLEX) {
                        iterator.remove();
                        if (iterator.hasNext()) {
                            StringBuilder value = new StringBuilder(iterator.next());
                            iterator.remove();
                            if (!value.toString().startsWith("\"")) {
                                flagValues.put(flag, value.toString());
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
                                flagValues.put(flag, value.toString().replace("\"", ""));
                            }
                        } else {
                            flagValues.put(flag, flag.defaultValue());
                        }
                        break;
                    }
                }
            }
        }

        for (Flag flag : this.flags) {
            if (!flagValues.containsKey(flag)) {
                if (flag.type() == FlagType.PRESENCE) {
                    flagValues.put(flag, flag.valueIfNotPresent());
                } else if (flag.type() == FlagType.COMPLEX) {
                    flagValues.put(flag, flag.defaultValue());
                }
            }
        }
        
        return new FlagResult(argsList.toArray(new String[0]), flagValues);
    }
}