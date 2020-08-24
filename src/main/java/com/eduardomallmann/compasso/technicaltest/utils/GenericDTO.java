package com.eduardomallmann.compasso.technicaltest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.dtfj.corereaders.zos.util.ObjectMap;

import java.util.Arrays;
import java.util.List;

public class GenericDTO {

    /**
     * Normalizes a string parameter informed with Camelcase and ignoring prepositions.
     *
     * @param field a String parameter
     *
     * @return a String.
     */
    protected String normalizeField(final String field) {
        final List<String> vowels = Arrays.asList("a", "e", "o");
        final String[] words = field.split("\\s");
        StringBuilder normalizeField = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                normalizeField
                        .append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase());
            } else {
                if ((word.length() <= 3 && word.length() > 1)
                            && (word.toLowerCase().startsWith("d") && vowels.contains(word.substring(1, 2)) && (word.length() == 2 || word.endsWith("s")))
                            && i != words.length - 1) {
                    normalizeField
                            .append(" ")
                            .append(word.toLowerCase());
                } else {
                    normalizeField
                            .append(" ")
                            .append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase());
                }
            }
        }
        return normalizeField.toString();
    }

    /**
     * Writes the object as in Json format as String.
     *
     * @return a String.
     */
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return null;
        }
    }
}
