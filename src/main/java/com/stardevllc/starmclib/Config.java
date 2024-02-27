package com.stardevllc.starmclib;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.Block;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Config {
    protected YamlDocument document;

    public Config(File file) {
        try {
            this.document = YamlDocument.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean reload() {
        try {
            return document.reload();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update() {
        try {
            return document.update();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean save() {
        try {
            return document.save();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String dump() {
        return document.dump();
    }

    public void setLoaderSettings(LoaderSettings loaderSettings) {
        document.setLoaderSettings(loaderSettings);
    }

    public void setDumperSettings(DumperSettings dumperSettings) {
        document.setDumperSettings(dumperSettings);
    }

    public void setGeneralSettings(GeneralSettings generalSettings) {
        document.setGeneralSettings(generalSettings);
    }

    public void setUpdaterSettings(UpdaterSettings updaterSettings) {
        document.setUpdaterSettings(updaterSettings);
    }

    public GeneralSettings getGeneralSettings() {
        return document.getGeneralSettings();
    }

    public DumperSettings getDumperSettings() {
        return document.getDumperSettings();
    }

    public UpdaterSettings getUpdaterSettings() {
        return document.getUpdaterSettings();
    }

    public LoaderSettings getLoaderSettings() {
        return document.getLoaderSettings();
    }

    public File getFile() {
        return document.getFile();
    }

    public boolean isRoot() {
        return document.isRoot();
    }

    public boolean isEmpty(boolean deep) {
        return document.isEmpty(deep);
    }

    public boolean isSection() {
        return document.isSection();
    }

    public YamlDocument getRoot() {
        return document.getRoot();
    }

    public Section getParent() {
        return document.getParent();
    }

    public Object getName() {
        return document.getName();
    }

    public String getNameAsString() {
        return document.getNameAsString();
    }

    public Route getNameAsRoute() {
        return document.getNameAsRoute();
    }

    public Route getRoute() {
        return document.getRoute();
    }

    public String getRouteAsString() {
        return document.getRouteAsString();
    }

    public Route getSubRoute(Object key) {
        return document.getSubRoute(key);
    }

    public boolean hasDefaults() {
        return document.hasDefaults();
    }

    public Object adaptKey(Object key) {
        return document.adaptKey(key);
    }

    public Set<Route> getRoutes(boolean deep) {
        return document.getRoutes(deep);
    }

    public Set<String> getRoutesAsStrings(boolean deep) {
        return document.getRoutesAsStrings(deep);
    }

    public Set<Object> getKeys() {
        return document.getKeys();
    }

    public Map<Route, Object> getRouteMappedValues(boolean deep) {
        return document.getRouteMappedValues(deep);
    }

    public Map<String, Object> getStringRouteMappedValues(boolean deep) {
        return document.getStringRouteMappedValues(deep);
    }

    public Map<Route, Block<?>> getRouteMappedBlocks(boolean deep) {
        return document.getRouteMappedBlocks(deep);
    }

    public Map<String, Block<?>> getStringRouteMappedBlocks(boolean deep) {
        return document.getStringRouteMappedBlocks(deep);
    }

    public boolean contains(Route route) {
        return document.contains(route);
    }

    public boolean contains(String route) {
        return document.contains(route);
    }

    public Section createSection(Route route) {
        return document.createSection(route);
    }

    public Section createSection(String route) {
        return document.createSection(route);
    }

    public void repopulate(Map<Object, Block<?>> mappings) {
        document.repopulate(mappings);
    }

    public void setAll(Map<Route, Object> mappings) {
        document.setAll(mappings);
    }

    public void set(Route route, Object value) {
        document.set(route, value);
    }

    public boolean remove(Route route) {
        return document.remove(route);
    }

    public boolean remove(String route) {
        return document.remove(route);
    }

    public void clear() {
        document.clear();
    }

    public Optional<Block<?>> getOptionalBlock(Route route) {
        return document.getOptionalBlock(route);
    }

    public Optional<Block<?>> getOptionalBlock(String route) {
        return document.getOptionalBlock(route);
    }

    public Block<?> getBlock(Route route) {
        return document.getBlock(route);
    }

    public Block<?> getBlock(String route) {
        return document.getBlock(route);
    }

    public Optional<Section> getParent(Route route) {
        return document.getParent(route);
    }

    public Optional<Section> getParent(String route) {
        return document.getParent(route);
    }

    public Optional<Object> getOptional(Route route) {
        return document.getOptional(route);
    }

    public Optional<Object> getOptional(String route) {
        return document.getOptional(route);
    }

    public Object get(Route route) {
        return document.get(route);
    }

    public Object get(Route route, Object def) {
        return document.get(route, def);
    }

    public Object get(String route, Object def) {
        return document.get(route, def);
    }

    public <T> Optional<T> getAsOptional(Route route, Class<T> clazz) {
        return document.getAsOptional(route, clazz);
    }

    public <T> Optional<T> getAsOptional(String route, Class<T> clazz) {
        return document.getAsOptional(route, clazz);
    }

    public <T> T getAs(Route route, Class<T> clazz) {
        return document.getAs(route, clazz);
    }

    public <T> T getAs(String route, Class<T> clazz) {
        return document.getAs(route, clazz);
    }

    public <T> T getAs(Route route, Class<T> clazz, T def) {
        return document.getAs(route, clazz, def);
    }

    public <T> T getAs(String route, Class<T> clazz, T def) {
        return document.getAs(route, clazz, def);
    }

    public <T> boolean is(Route route, Class<T> clazz) {
        return document.is(route, clazz);
    }

    public <T> boolean is(String route, Class<T> clazz) {
        return document.is(route, clazz);
    }

    public Optional<Section> getOptionalSection(Route route) {
        return document.getOptionalSection(route);
    }

    public Optional<Section> getOptionalSection(String route) {
        return document.getOptionalSection(route);
    }

    public Section getSection(Route route) {
        return document.getSection(route);
    }

    public Section getSection(String route) {
        return document.getSection(route);
    }

    public Section getSection(Route route, Section def) {
        return document.getSection(route, def);
    }

    public Section getSection(String route, Section def) {
        return document.getSection(route, def);
    }

    public boolean isSection(Route route) {
        return document.isSection(route);
    }

    public boolean isSection(String route) {
        return document.isSection(route);
    }

    public Optional<String> getOptionalString(Route route) {
        return document.getOptionalString(route);
    }

    public Optional<String> getOptionalString(String route) {
        return document.getOptionalString(route);
    }

    public String getString(Route route) {
        return document.getString(route);
    }

    public String getString(String route) {
        return document.getString(route);
    }

    public String getString(Route route, String def) {
        return document.getString(route, def);
    }

    public String getString(String route, String def) {
        return document.getString(route, def);
    }

    public boolean isString(Route route) {
        return document.isString(route);
    }

    public boolean isString(String route) {
        return document.isString(route);
    }

    public <T extends Enum<T>> Optional<T> getOptionalEnum(Route route, Class<T> clazz) {
        return document.getOptionalEnum(route, clazz);
    }

    public <T extends Enum<T>> Optional<T> getOptionalEnum(String route, Class<T> clazz) {
        return document.getOptionalEnum(route, clazz);
    }

    public <T extends Enum<T>> T getEnum(Route route, Class<T> clazz) {
        return document.getEnum(route, clazz);
    }

    public <T extends Enum<T>> T getEnum(String route, Class<T> clazz) {
        return document.getEnum(route, clazz);
    }

    public <T extends Enum<T>> T getEnum(Route route, Class<T> clazz, T def) {
        return document.getEnum(route, clazz, def);
    }

    public <T extends Enum<T>> T getEnum(String route, Class<T> clazz, T def) {
        return document.getEnum(route, clazz, def);
    }

    public <T extends Enum<T>> boolean isEnum(Route route, Class<T> clazz) {
        return document.isEnum(route, clazz);
    }

    public <T extends Enum<T>> boolean isEnum(String route, Class<T> clazz) {
        return document.isEnum(route, clazz);
    }

    public Optional<Character> getOptionalChar(Route route) {
        return document.getOptionalChar(route);
    }

    public Optional<Character> getOptionalChar(String route) {
        return document.getOptionalChar(route);
    }

    public Character getChar(Route route) {
        return document.getChar(route);
    }

    public Character getChar(String route) {
        return document.getChar(route);
    }

    public Character getChar(Route route, Character def) {
        return document.getChar(route, def);
    }

    public Character getChar(String route, Character def) {
        return document.getChar(route, def);
    }

    public boolean isChar(Route route) {
        return document.isChar(route);
    }

    public boolean isChar(String route) {
        return document.isChar(route);
    }

    public Optional<Number> getOptionalNumber(Route route) {
        return document.getOptionalNumber(route);
    }

    public Optional<Number> getOptionalNumber(String route) {
        return document.getOptionalNumber(route);
    }

    public Number getNumber(Route route) {
        return document.getNumber(route);
    }

    public Number getNumber(String route) {
        return document.getNumber(route);
    }

    public Number getNumber(Route route, Number def) {
        return document.getNumber(route, def);
    }

    public Number getNumber(String route, Number def) {
        return document.getNumber(route, def);
    }

    public boolean isNumber(Route route) {
        return document.isNumber(route);
    }

    public boolean isNumber(String route) {
        return document.isNumber(route);
    }

    public Optional<Integer> getOptionalInt(Route route) {
        return document.getOptionalInt(route);
    }

    public Optional<Integer> getOptionalInt(String route) {
        return document.getOptionalInt(route);
    }

    public Integer getInt(Route route) {
        return document.getInt(route);
    }

    public Integer getInt(String route) {
        return document.getInt(route);
    }

    public Integer getInt(Route route, Integer def) {
        return document.getInt(route, def);
    }

    public Integer getInt(String route, Integer def) {
        return document.getInt(route, def);
    }

    public boolean isInt(Route route) {
        return document.isInt(route);
    }

    public boolean isInt(String route) {
        return document.isInt(route);
    }

    public Optional<BigInteger> getOptionalBigInt(Route route) {
        return document.getOptionalBigInt(route);
    }

    public Optional<BigInteger> getOptionalBigInt(String route) {
        return document.getOptionalBigInt(route);
    }

    public BigInteger getBigInt(Route route) {
        return document.getBigInt(route);
    }

    public BigInteger getBigInt(String route) {
        return document.getBigInt(route);
    }

    public BigInteger getBigInt(Route route, BigInteger def) {
        return document.getBigInt(route, def);
    }

    public BigInteger getBigInt(String route, BigInteger def) {
        return document.getBigInt(route, def);
    }

    public boolean isBigInt(Route route) {
        return document.isBigInt(route);
    }

    public boolean isBigInt(String route) {
        return document.isBigInt(route);
    }

    public Optional<Boolean> getOptionalBoolean(Route route) {
        return document.getOptionalBoolean(route);
    }

    public Optional<Boolean> getOptionalBoolean(String route) {
        return document.getOptionalBoolean(route);
    }

    public Boolean getBoolean(Route route) {
        return document.getBoolean(route);
    }

    public Boolean getBoolean(String route) {
        return document.getBoolean(route);
    }

    public Boolean getBoolean(Route route, Boolean def) {
        return document.getBoolean(route, def);
    }

    public Boolean getBoolean(String route, Boolean def) {
        return document.getBoolean(route, def);
    }

    public boolean isBoolean(Route route) {
        return document.isBoolean(route);
    }

    public boolean isBoolean(String route) {
        return document.isBoolean(route);
    }

    public Optional<Double> getOptionalDouble(Route route) {
        return document.getOptionalDouble(route);
    }

    public Optional<Double> getOptionalDouble(String route) {
        return document.getOptionalDouble(route);
    }

    public Double getDouble(Route route) {
        return document.getDouble(route);
    }

    public Double getDouble(String route) {
        return document.getDouble(route);
    }

    public Double getDouble(Route route, Double def) {
        return document.getDouble(route, def);
    }

    public Double getDouble(String route, Double def) {
        return document.getDouble(route, def);
    }

    public boolean isDouble(Route route) {
        return document.isDouble(route);
    }

    public boolean isDouble(String route) {
        return document.isDouble(route);
    }

    public Optional<Float> getOptionalFloat(Route route) {
        return document.getOptionalFloat(route);
    }

    public Optional<Float> getOptionalFloat(String route) {
        return document.getOptionalFloat(route);
    }

    public Float getFloat(Route route) {
        return document.getFloat(route);
    }

    public Float getFloat(String route) {
        return document.getFloat(route);
    }

    public Float getFloat(Route route, Float def) {
        return document.getFloat(route, def);
    }

    public Float getFloat(String route, Float def) {
        return document.getFloat(route, def);
    }

    public boolean isFloat(Route route) {
        return document.isFloat(route);
    }

    public boolean isFloat(String route) {
        return document.isFloat(route);
    }

    public Optional<Byte> getOptionalByte(Route route) {
        return document.getOptionalByte(route);
    }

    public Optional<Byte> getOptionalByte(String route) {
        return document.getOptionalByte(route);
    }

    public Byte getByte(Route route) {
        return document.getByte(route);
    }

    public Byte getByte(String route) {
        return document.getByte(route);
    }

    public Byte getByte(Route route, Byte def) {
        return document.getByte(route, def);
    }

    public Byte getByte(String route, Byte def) {
        return document.getByte(route, def);
    }

    public boolean isByte(Route route) {
        return document.isByte(route);
    }

    public boolean isByte(String route) {
        return document.isByte(route);
    }

    public Optional<Long> getOptionalLong(Route route) {
        return document.getOptionalLong(route);
    }

    public Optional<Long> getOptionalLong(String route) {
        return document.getOptionalLong(route);
    }

    public Long getLong(Route route) {
        return document.getLong(route);
    }

    public Long getLong(String route) {
        return document.getLong(route);
    }

    public Long getLong(Route route, Long def) {
        return document.getLong(route, def);
    }

    public Long getLong(String route, Long def) {
        return document.getLong(route, def);
    }

    public boolean isLong(Route route) {
        return document.isLong(route);
    }

    public boolean isLong(String route) {
        return document.isLong(route);
    }

    public Optional<Short> getOptionalShort(Route route) {
        return document.getOptionalShort(route);
    }

    public Optional<Short> getOptionalShort(String route) {
        return document.getOptionalShort(route);
    }

    public Short getShort(Route route) {
        return document.getShort(route);
    }

    public Short getShort(String route) {
        return document.getShort(route);
    }

    public Short getShort(Route route, Short def) {
        return document.getShort(route, def);
    }

    public Short getShort(String route, Short def) {
        return document.getShort(route, def);
    }

    public boolean isShort(Route route) {
        return document.isShort(route);
    }

    public boolean isShort(String route) {
        return document.isShort(route);
    }

    public boolean isDecimal(Route route) {
        return document.isDecimal(route);
    }

    public boolean isDecimal(String route) {
        return document.isDecimal(route);
    }

    public Optional<List<?>> getOptionalList(Route route) {
        return document.getOptionalList(route);
    }

    public Optional<List<?>> getOptionalList(String route) {
        return document.getOptionalList(route);
    }

    public List<?> getList(Route route) {
        return document.getList(route);
    }

    public List<?> getList(String route) {
        return document.getList(route);
    }

    public List<?> getList(Route route, List<?> def) {
        return document.getList(route, def);
    }

    public List<?> getList(String route, List<?> def) {
        return document.getList(route, def);
    }

    public boolean isList(Route route) {
        return document.isList(route);
    }

    public boolean isList(String route) {
        return document.isList(route);
    }

    public Optional<List<String>> getOptionalStringList(Route route) {
        return document.getOptionalStringList(route);
    }

    public Optional<List<String>> getOptionalStringList(String route) {
        return document.getOptionalStringList(route);
    }

    public List<String> getStringList(Route route, List<String> def) {
        return document.getStringList(route, def);
    }

    public List<String> getStringList(String route, List<String> def) {
        return document.getStringList(route, def);
    }

    public List<String> getStringList(Route route) {
        return document.getStringList(route);
    }

    public List<String> getStringList(String route) {
        return document.getStringList(route);
    }

    public Optional<List<Integer>> getOptionalIntList(Route route) {
        return document.getOptionalIntList(route);
    }

    public Optional<List<Integer>> getOptionalIntList(String route) {
        return document.getOptionalIntList(route);
    }

    public List<Integer> getIntList(Route route, List<Integer> def) {
        return document.getIntList(route, def);
    }

    public List<Integer> getIntList(String route, List<Integer> def) {
        return document.getIntList(route, def);
    }

    public List<Integer> getIntList(Route route) {
        return document.getIntList(route);
    }

    public List<Integer> getIntList(String route) {
        return document.getIntList(route);
    }

    public Optional<List<BigInteger>> getOptionalBigIntList(Route route) {
        return document.getOptionalBigIntList(route);
    }

    public Optional<List<BigInteger>> getOptionalBigIntList(String route) {
        return document.getOptionalBigIntList(route);
    }

    public List<BigInteger> getBigIntList(Route route, List<BigInteger> def) {
        return document.getBigIntList(route, def);
    }

    public List<BigInteger> getBigIntList(String route, List<BigInteger> def) {
        return document.getBigIntList(route, def);
    }

    public List<BigInteger> getBigIntList(Route route) {
        return document.getBigIntList(route);
    }

    public List<BigInteger> getBigIntList(String route) {
        return document.getBigIntList(route);
    }

    public Optional<List<Byte>> getOptionalByteList(Route route) {
        return document.getOptionalByteList(route);
    }

    public Optional<List<Byte>> getOptionalByteList(String route) {
        return document.getOptionalByteList(route);
    }

    public List<Byte> getByteList(Route route, List<Byte> def) {
        return document.getByteList(route, def);
    }

    public List<Byte> getByteList(String route, List<Byte> def) {
        return document.getByteList(route, def);
    }

    public List<Byte> getByteList(Route route) {
        return document.getByteList(route);
    }

    public List<Byte> getByteList(String route) {
        return document.getByteList(route);
    }

    public Optional<List<Long>> getOptionalLongList(Route route) {
        return document.getOptionalLongList(route);
    }

    public Optional<List<Long>> getOptionalLongList(String route) {
        return document.getOptionalLongList(route);
    }

    public List<Long> getLongList(Route route, List<Long> def) {
        return document.getLongList(route, def);
    }

    public List<Long> getLongList(String route, List<Long> def) {
        return document.getLongList(route, def);
    }

    public List<Long> getLongList(Route route) {
        return document.getLongList(route);
    }

    public List<Long> getLongList(String route) {
        return document.getLongList(route);
    }

    public Optional<List<Double>> getOptionalDoubleList(Route route) {
        return document.getOptionalDoubleList(route);
    }

    public Optional<List<Double>> getOptionalDoubleList(String route) {
        return document.getOptionalDoubleList(route);
    }

    public List<Double> getDoubleList(Route route, List<Double> def) {
        return document.getDoubleList(route, def);
    }

    public List<Double> getDoubleList(String route, List<Double> def) {
        return document.getDoubleList(route, def);
    }

    public List<Double> getDoubleList(Route route) {
        return document.getDoubleList(route);
    }

    public List<Double> getDoubleList(String route) {
        return document.getDoubleList(route);
    }

    public Optional<List<Float>> getOptionalFloatList(Route route) {
        return document.getOptionalFloatList(route);
    }

    public Optional<List<Float>> getOptionalFloatList(String route) {
        return document.getOptionalFloatList(route);
    }

    public List<Float> getFloatList(Route route, List<Float> def) {
        return document.getFloatList(route, def);
    }

    public List<Float> getFloatList(String route, List<Float> def) {
        return document.getFloatList(route, def);
    }

    public List<Float> getFloatList(Route route) {
        return document.getFloatList(route);
    }

    public List<Float> getFloatList(String route) {
        return document.getFloatList(route);
    }

    public Optional<List<Short>> getOptionalShortList(Route route) {
        return document.getOptionalShortList(route);
    }

    public Optional<List<Short>> getOptionalShortList(String route) {
        return document.getOptionalShortList(route);
    }

    public List<Short> getShortList(Route route, List<Short> def) {
        return document.getShortList(route, def);
    }

    public List<Short> getShortList(String route, List<Short> def) {
        return document.getShortList(route, def);
    }

    public List<Short> getShortList(Route route) {
        return document.getShortList(route);
    }

    public List<Short> getShortList(String route) {
        return document.getShortList(route);
    }

    public Optional<List<Map<?, ?>>> getOptionalMapList(Route route) {
        return document.getOptionalMapList(route);
    }

    public Optional<List<Map<?, ?>>> getOptionalMapList(String route) {
        return document.getOptionalMapList(route);
    }

    public List<Map<?, ?>> getMapList(Route route, List<Map<?, ?>> def) {
        return document.getMapList(route, def);
    }

    public List<Map<?, ?>> getMapList(String route, List<Map<?, ?>> def) {
        return document.getMapList(route, def);
    }

    public List<Map<?, ?>> getMapList(Route route) {
        return document.getMapList(route);
    }

    public List<Map<?, ?>> getMapList(String route) {
        return document.getMapList(route);
    }


    public List<String> getComments() {
        return document.getComments();
    }

    public void setComments(List<String> comments) {
        document.setComments(comments);
    }

    public void removeComments() {
        document.removeComments();
    }

    public void addComments(List<String> comments) {
        document.addComments(comments);
    }

    public void addComment(String comment) {
        document.addComment(comment);
    }

    public void setIgnored(boolean ignored) {
        document.setIgnored(ignored);
    }

    public boolean isIgnored() {
        return document.isIgnored();
    }

    public Map<Object, Block<?>> getStoredValue() {
        return document.getStoredValue();
    }

    public void set(String path, Object value) {
        this.document.set(path, value);
    }

    public Object get(String path) {
        return this.document.get(path);
    }

    public void addDefault(String path, Object value, String... comments) {
        if (!this.document.contains(path)) {
            this.document.set(path, value);
            if (comments != null) {
                this.document.getBlock(path).addComments(List.of(comments));
            }
        }
    }

    public void addDefault(Route path, Object value, String... comments) {
        if (!this.document.contains(path)) {
            this.document.set(path, value);
            if (comments != null) {
                this.document.getBlock(path).addComments(List.of(comments));
            }
        }
    }
}