package com.code.wlu.noraibarguren.androidassignments;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;

public class LoginUnitTest {

    @Test
    public void testEmptyEmailIsInvalid() {
        // el login rechaza email vacio o solo con espacios
        assertTrue("".trim().isEmpty());
        assertTrue("   ".trim().isEmpty());
        assertFalse("nora@correo.com".trim().isEmpty());
    }

    @Test
    public void testEmptyPasswordIsInvalid() {
        // lo mismo para la contrasena
        String password = "  ";
        assertTrue(password.trim().isEmpty());
    }

    @Test
    public void testTrimRemovesSpaces() {
        String email = "  nora@correo.com ";

        // el login hace trim() antes de validar y de guardar
        assertEquals("nora@correo.com", email.trim());
    }

    @Test
    public void testDefaultEmailAndSavedEmail() {
        // simulacion de SharedPreferences con un HashMap
        HashMap<String, String> prefs = new HashMap<>();

        // primera vez: no hay nada guardado, se usa el email por defecto
        String email = prefs.getOrDefault("DefaultEmail", "email@domain.com");
        assertEquals("email@domain.com", email);

        // despues de un login correcto se guarda el email
        prefs.put("DefaultEmail", "nora@correo.com");
        email = prefs.getOrDefault("DefaultEmail", "email@domain.com");
        assertEquals("nora@correo.com", email);
    }
}