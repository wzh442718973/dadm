package com.glaring.colourful.bully.base;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDump {

    void dump(PrintWriter writer);

    String toString();


    static final class Builder {

        private static String getType(Class clazz, Object object) {
            if (object != null) {
                if (clazz == null)
                    clazz = object.getClass();
            }
            return (clazz == null ? "null" : clazz.getName()) + "@" + (object == null ? "null" : Integer.toHexString(object.hashCode()));
        }

        public static IDump build(Object obj, int superDepth, int depth) {
            if (depth <= 0) {
                return new BASE(obj);
            } else if (obj == null) {
                return new BASE(obj);
            } else if (XObject.baseObject(obj)) {
                return new BASE(obj);
            } else if (List.class.isInstance(obj)) {
                return new ARRAY((List) obj, superDepth, depth);
            } else if (Object[].class.isInstance(obj)) {
                return new ARRAY((Object[]) obj, superDepth, depth);
            } else if (Set.class.isInstance(obj)) {
                return new ARRAY((Set) obj, superDepth, depth);
            } else if (Map.class.isInstance(obj)) {
                return new ARRAY((Map) obj, superDepth, depth);
            } else {
                return new MAP(true, obj, superDepth, depth);
            }
        }
    }

    static final class BASE implements IDump {
        private final Object obj;

        BASE(Object obj) {
            this.obj = obj;
        }

        @Override
        public void dump(PrintWriter writer) {
            writer.append(toString());
            writer.flush();
        }

        @Override
        public String toString() {
            if (obj == null) {
                return "\"null\"";
            } else if (XObject.baseObject(obj)) {
                return "\"" + obj.toString() + "\"";
            } else {
                return "\"" + Builder.getType(null, obj) + "\"";
            }
        }
    }

    static final class ARRAY extends ArrayList<IDump> implements IDump {

        //[{'type@hash': chidls},]
        ARRAY(Object[] array, int superDepth, int depth) {
            super(array.length);
            for (Object obj : array) {
                add(new MAP(obj, superDepth, depth));
//                add(Builder.build(obj));
            }
        }


        //[{'type@hash': chidls},]
        ARRAY(List list, int superDepth, int depth) {
            super(list.size());
            for (Object obj : list) {
                add(new MAP(obj, superDepth, depth));
            }
        }

        //[{'type@hash': chidls},]
        ARRAY(Set set, int superDepth, int depth) {
            super(set.size());
            for (Object obj : set) {
                add(new MAP(obj, superDepth, depth));
            }
        }

        //[{'name':"", 'type':"", 'value':childs},]
        ARRAY(Map<?, ?> map, int superDepth, int depth) {
            super(map.size());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                add(new MAP(entry.getKey(), entry.getValue(), superDepth, depth));
            }
        }

        //[{'name':"", 'type':"", 'value':childs}]
        ARRAY(Field[] fields, Object object, int superDepth, int depth) {
            super(fields.length);
            for (Field field : fields) {
                add(new MAP(field, object, superDepth, depth));
            }
        }

        //[methods, .., ..]
        ARRAY(Method[] methods, Object object, int superDepth, int depth) {
            super(methods.length);
            for (Method method : methods) {
                add(new BASE(method));
            }
        }

        //[constructors, .., ..]
        ARRAY(Constructor[] constructors, Object object, int superDepth, int depth) {
            super(constructors.length);
            for (Constructor constructor : constructors) {
                add(new BASE(constructor));
            }
        }


        @Override
        public void dump(PrintWriter writer) {
            boolean first = true;
            writer.println("[\n");
            for (IDump dump : this) {
                if (first) {
                    first = false;
                } else {
                    writer.append(',');
                }
                dump.dump(writer);
                writer.print("\n");
                writer.flush();
            }
            writer.print("]");
            writer.flush();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            sb.append("[");
            for (IDump dump : this) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append(dump.toString());
            }
            sb.append("]");
            return sb.toString();
        }
    }

    static final class MAP extends HashMap<String, IDump> implements IDump {


        //{'name':childs, 'type':"", 'value':childs}
        private MAP(Object key, Object value, int superDepth, int depth) {
            super(3);
            put("name", new BASE(key));
            put("type", new BASE(Builder.getType(null, value)));
            put("value", Builder.build(value, superDepth, depth - 1));
        }

        //{'name':"", 'type':"", 'value':childs}
        private MAP(Field field, Object object, int superDepth, int depth) {
            super(3);
            final String fieldName = field.getName();
            final Class fieldType = field.getType();
            Object fieldVal = null;
            try {
                fieldVal = field.get(object);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (XObject.baseClass(fieldType)) {
                put(fieldType.getSimpleName(), new BASE(fieldVal));
            } else {
                put("name", new BASE(fieldName));
                put("type", new BASE(Builder.getType(fieldType, fieldVal)));
                put("value", Builder.build(fieldVal, superDepth, depth - 1));
            }
        }


        public MAP(Object object, int superDepth, int depth) {
            this(false, object, superDepth, depth);
        }

        private MAP(boolean childs, Object object, int superDepth, int depth) {
            super(!childs ? 1 : 16);
            if (childs) {
                final Class clazz = object.getClass();

                final Field[] fields = clazz.getDeclaredFields();
                final Method[] methods = clazz.getDeclaredMethods();
                final Constructor[] constructors = clazz.getDeclaredConstructors();

                put("Fields", new ARRAY(fields, object, superDepth, depth));
                put("Methods", new ARRAY(methods, object, superDepth, depth));
                put("Constructors", new ARRAY(constructors, object, superDepth, depth));
            } else {
                if (object == null) {
                    put("object", new BASE(null));
                } else {
                    put(Builder.getType(null, object), Builder.build(object, superDepth, depth));
                }
            }
        }

        @Override
        public void dump(PrintWriter writer) {
            boolean first = true;
            writer.println("{");
            for (Entry<String, IDump> entry : entrySet()) {
                if (first) {
                    first = false;
                } else {
                    writer.append(',');
                }
                writer.append("\"").append(entry.getKey()).append("\":");
                entry.getValue().dump(writer);
                writer.print("\n");
                writer.flush();
            }
            writer.print("}");
            writer.flush();
        }

        @Override
        public String toString() {
            boolean first = true;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (Entry<String, IDump> entry : entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(',');
                }
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(entry.getValue().toString());

            }
            sb.append("}");

            return sb.toString();
        }
    }

}
