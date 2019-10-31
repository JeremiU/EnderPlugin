package io.github.rookietec9.enderplugin.API;

import com.google.common.collect.ForwardingMultimap;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author insou22
 * @version 14.4.1
 * @since 14.4.1
 *
 * https://github.com/insou22/Skulls/blob/master/src/main/java/co/insou/skulls/SkullMaker.java
 */
public class SkullMaker {

    private String url;
    private String name;
    private List<String> lore = new ArrayList<>();

    public SkullMaker withSkinUrl(String url, String name) {
        this.url = url;
        this.name = name;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (url != null) loadProfile(meta, url);
        if (name != null) meta.setDisplayName(name);
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    private void loadProfile(ItemMeta meta, String url) {

        Class<?> profileClass = Reflection.getClass("com.mojang.authlib.GameProfile");

        Constructor<?> profileConstructor = Reflection.getDeclaredConstructor(profileClass, UUID.class, String.class);

        Object profile = Reflection.newInstance(profileConstructor, UUID.randomUUID(), null);

        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());

        Method getPropertiesMethod = Reflection.getDeclaredMethod(profileClass, "getProperties");

        Object propertyMap = Reflection.invoke(getPropertiesMethod, profile);

        Class<?> propertyClass = Reflection.getClass("com.mojang.authlib.properties.Property");

        Reflection.invoke(
                Reflection.getDeclaredMethod(
                        ForwardingMultimap.class, "put", Object.class, Object.class
                ),
                propertyMap,
                "textures",
                Reflection.newInstance(Reflection.getDeclaredConstructor(propertyClass, String.class, String.class), "textures", new String(encodedData))
        );

        Reflection.setField("profile", meta, profile);
    }

    private static final class Reflection {

        private static Class<?> getClass(String forName) {
            try {
                return Class.forName(forName);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        private static <T> Constructor<T> getDeclaredConstructor(Class<T> clazz, Class<?>... params) {
            try {
                return clazz.getDeclaredConstructor(params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static <T> T newInstance(Constructor<T> constructor, Object... params) {
            try {
                return constructor.newInstance(params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                return null;
            }
        }

        private static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... params) {
            try {
                return clazz.getDeclaredMethod(name, params);
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        private static Object invoke(Method method, Object object, Object... params) {
            method.setAccessible(true);
            try {
                return method.invoke(object, params);
            } catch (InvocationTargetException | IllegalAccessException e) {
                return null;
            }
        }

        private static void setField(String name, Object instance, Object value) {
            Field field = getDeclaredField(instance.getClass(), name);
            field.setAccessible(true);
            try {
                field.set(instance, value);
            } catch (IllegalAccessException ignored) {}
        }

        private static Field getDeclaredField(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                return null;
            }
        }
    }
}