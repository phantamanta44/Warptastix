package io.github.phantamanta44.warptastix.command.opt;

import io.github.phantamanta44.warptastix.WTXLang;
import io.github.phantamanta44.warptastix.command.WTXCommandException;
import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OptParser {

    private final String[] source;
    private final Map<String, MutableBoolean> flags;
    private String[] dest;

    public OptParser(String[] args) {
        this.source = args;
        this.flags = new HashMap<>();
    }

    public OptParser with(String flag) {
        flags.put(flag, new MutableBoolean(false));
        return this;
    }

    public OptParser parse() throws WTXCommandException {
        int i = 0;
        while (i < source.length) {
            if (source[i].startsWith("-")) {
                MutableBoolean flag = flags.get(source[i].substring(1));
                if (flag == null)
                    throw new WTXCommandException(WTXLang.prefix("command.unknownflag", source[i]));
                flag.setValue(true);
                i++;
            } else {
                break;
            }
        }
        dest = Arrays.copyOfRange(source, i, source.length);
        return this;
    }

    public boolean has(String flag) {
        return flags.get(flag).booleanValue();
    }

    public String[] getArgs() {
        return dest;
    }

}
