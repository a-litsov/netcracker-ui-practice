package com.edu_netcracker.nn.adlitsov.ui.homework1;

import javax.swing.*;

public class InputVerifiers {

    public static class BookNameVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            if (!(input instanceof JTextField)) {
                return false;
            }

            String name = ((JTextField) input).getText();
            return Book.isNameValid(name);
        }
    }

    public static class AuthorNameVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            if (!(input instanceof JTextField)) {
                return false;
            }

            String name = ((JTextField) input).getText();
            return Author.isNameValid(name);
        }
    }

    public static class AuthorEmailVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            if (!(input instanceof JTextField)) {
                return false;
            }

            String email = ((JTextField) input).getText();
            return Author.isEmailValid(email);
        }
    }
}
