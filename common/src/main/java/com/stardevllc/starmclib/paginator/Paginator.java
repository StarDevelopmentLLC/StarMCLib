package com.stardevllc.starmclib.paginator;

import com.stardevllc.starlib.builder.IBuilder;
import com.stardevllc.starlib.converter.string.StringConverter;
import com.stardevllc.starlib.function.TriFunction;
import com.stardevllc.starmclib.actors.Actor;

import java.util.*;
import java.util.function.BiFunction;

public class Paginator<T> {
    public interface Vars {
        String replace(String text, Actor actor, Paginator<?> paginator);
        String replace(String text, Actor actor, Paginator<?> paginator, Object element);
    }
    
    public enum DefaultVars implements Vars {
        CURRENT_PAGE("{current_page}", (actor, paginator) -> String.valueOf(paginator.getCurrentPage(actor))), 
        NEXT_PAGE("{next_page}", (actor, paginator) -> {
            if (paginator.getCurrentPage(actor) == paginator.getTotalPages()) {
                return String.valueOf(-1);
            }
            
            return String.valueOf(paginator.getCurrentPage(actor) + 1);
        }), 
        TOTAL_PAGES("{total_pages}", (actor, paginator) -> String.valueOf(paginator.getTotalPages())), 
        ELEMENT("{element}", (actor, paginator, object) -> {
            StringConverter<Object> converter = (StringConverter<Object>) paginator.getConverter();
            if (converter != null) {
                return converter.convertFrom(object);
            }
            
            return String.valueOf(object);
        });
        
        private final String value;
        private final BiFunction<Actor, Paginator<?>, String> mapper;
        private final TriFunction<Actor, Paginator<?>, Object, String> elementMapper;
        
        DefaultVars(String value) {
            this(value, null, null);
        }
        
        DefaultVars(String value, BiFunction<Actor, Paginator<?>, String> mapper) {
            this(value, mapper, null);
        }
        
        DefaultVars(String value, TriFunction<Actor, Paginator<?>, Object, String> elementMapper) {
            this(value, null, elementMapper);
        }
        
        DefaultVars(String value, BiFunction<Actor, Paginator<?>, String> mapper, TriFunction<Actor, Paginator<?>, Object, String> elementMapper) {
            this.value = value;
            this.mapper = mapper;
            this.elementMapper = elementMapper;
        }
        
        public String replace(String text, Actor actor, Paginator<?> paginator) {
            if (mapper != null) {
                return text.replace(this.value, mapper.apply(actor, paginator));
            }
            
            return text;
        }
        
        @Override
        public String replace(String text, Actor actor, Paginator<?> paginator, Object element) {
            if (elementMapper != null) {
                return text.replace(this.value, elementMapper.apply(actor, paginator, element));
            }
            
            return text;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    protected final BiFunction<Paginator<T>, Actor, String> header;
    protected final BiFunction<Paginator<T>, Actor, String> footer;
    protected final String lineFormat;
    protected final Collection<T> elements;
    protected final int elementsPerPage;
    protected final StringConverter<T> converter;
    protected final Set<Vars> supportedVars = new HashSet<>(List.of(DefaultVars.values()));
    
    protected final Map<Actor, Integer> actorCurrentPages = new HashMap<>();
    
    public Paginator(BiFunction<Paginator<T>, Actor, String> header, BiFunction<Paginator<T>, Actor, String> footer, String lineFormat, Collection<T> elements, int elementsPerPage, StringConverter<T> converter, Set<Vars> supportedVars) {
        this.header = header;
        this.footer = footer;
        this.lineFormat = lineFormat;
        this.elements = elements;
        this.elementsPerPage = elementsPerPage;
        this.converter = converter;
        this.supportedVars.addAll(supportedVars);
    }
    
    protected int validatePage(int page) {
        return Math.clamp(page, 1, getTotalPages());
    }
    
    public int getCurrentPage(Actor actor) {
        return this.actorCurrentPages.computeIfAbsent(actor, a -> 1);
    }
    
    public void display(Actor actor) {
        display(actor, getCurrentPage(actor));
    }
    
    public void display(Actor actor, int page) {
        page = validatePage(page);
        
        String header;
        if (this.header != null) {
            header = this.header.apply(this, actor);
        } else {
            header = "";
        }
        
        String footer;
        if (this.footer != null) {
            footer = this.footer.apply(this, actor);
        } else {
            footer = "";
        }
        
        for (Vars var : supportedVars) {
            header = var.replace(header, actor, this);
            footer = var.replace(footer, actor, this);
        }
        
        int offset = (page - 1) * elementsPerPage;
        int resultEnd = offset + elementsPerPage;
        if (resultEnd >= this.elements.size()) {
            resultEnd = this.elements.size();
        }
        
        if (header != null && !header.isEmpty()) {
            actor.sendColoredMessage(header);
        }
        
        List<T> elements = new ArrayList<>(this.elements);
        for (int i = offset; i < resultEnd; i++) {
            T element = elements.get(i);
            String lineFormat = this.lineFormat;
            for (Vars var : supportedVars) {
                lineFormat = var.replace(lineFormat, actor, this);
                lineFormat = var.replace(lineFormat, actor, this, element);
            }
            actor.sendColoredMessage(lineFormat);
        }
        
        if (footer != null && !footer.isEmpty()) {
            actor.sendColoredMessage(footer);
        }
    }
    
    public BiFunction<Paginator<T>, Actor, String> getHeader() {
        return header;
    }
    
    public BiFunction<Paginator<T>, Actor, String> getFooter() {
        return footer;
    }
    
    public String getLineFormat() {
        return lineFormat;
    }
    
    public Collection<T> getElements() {
        return elements;
    }
    
    public int getElementsPerPage() {
        return elementsPerPage;
    }
    
    public StringConverter<T> getConverter() {
        return converter;
    }
    
    public Set<Paginator.Vars> getSupportedVars() {
        return new HashSet<>(supportedVars);
    }
    
    public int getTotalPages() {
        int totalElements = this.elements.size();
        int pages = totalElements / elementsPerPage;
        int leftOver = totalElements % elementsPerPage;
        
        if (leftOver > 0) {
            pages++;
        }
        
        return pages;
    }
    
    public static class Builder<T> implements IBuilder<Paginator<T>, Builder<T>> {
        protected BiFunction<Paginator<T>, Actor, String> header;
        protected BiFunction<Paginator<T>, Actor, String> footer;
        protected String lineFormat;
        protected Collection<T> elements;
        protected int elementsPerPage;
        protected StringConverter<T> converter;
        protected final Set<Vars> supportedVars = new HashSet<>();
        
        public Builder() {}
        
        public Builder(Builder<T> builder) {
            this.header = builder.header;
            this.footer = builder.footer;
            this.lineFormat = builder.lineFormat;
            this.elements = builder.elements;
            this.elementsPerPage = builder.elementsPerPage;
            this.converter = builder.converter;
            this.supportedVars.addAll(builder.supportedVars);
        }
        
        public Builder<T> header(BiFunction<Paginator<T>, Actor, String> header) {
            this.header = header;
            return self();
        }
        
        public Builder<T> footer(BiFunction<Paginator<T>, Actor, String> footer) {
            this.footer = footer;
            return self();
        }
        
        public Builder<T> lineFormat(String lineFormat) {
            this.lineFormat = lineFormat;
            return self();
        }
        
        public Builder<T> elements(Collection<T> elements) {
            this.elements = elements;
            return self();
        }
        
        public Builder<T> elementsPerPage(int elementsPerPage) {
            this.elementsPerPage = elementsPerPage;
            return self();
        }
        
        public Builder<T> converter(StringConverter<T> converter) {
            this.converter = converter;
            return self();
        }
        
        public Builder<T> supportedVars(Vars var, Vars... vars) {
            this.supportedVars.add(var);
            if (vars != null) {
                for (Vars v : vars) {
                    this.supportedVars.add(var);
                }
            }
            
            return self();
        }
        
        @Override
        public Paginator<T> build() {
            if (elements == null) {
                this.elements = new LinkedList<>();
            }
            
            if (elementsPerPage < 1) {
                elementsPerPage = 1;
            }
            
            return new Paginator<>(header, footer, lineFormat, elements, elementsPerPage, converter, supportedVars);
        }
        
        @Override
        public Builder<T> clone() {
            return new Builder<>(this);
        }
    }
}