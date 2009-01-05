package com.krasama.music.fractal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Parser
{
    private static abstract class Statement
    {
    }

    private static class NameStatement extends Statement
    {
        final String type;
        final String name;

        NameStatement(String type, String name)
        {
            this.type = type;
            this.name = name;
        }
    }

    private static class ValueStatement extends Statement
    {
        final String key;
        final String value;

        ValueStatement(String key, String value)
        {
            this.key = key;
            this.value = value;
        }
    }

    public static Map<String, Song> parseSongs(InputStream stream) throws Exception
    {
        Map<String, Song> songs = new HashMap<String, Song>();
        Map<String, Scale> scales = new HashMap<String, Scale>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String type = null;
        String name = null;
        Map<String, String> values = null;
        while ((line = reader.readLine()) != null)
        {
            Statement statement = parseStatement(line);
            if (statement instanceof NameStatement)
            {
                if (type != null)
                {
                    buildObject(scales, songs, type, name, values);
                }
                NameStatement nameStatement = (NameStatement) statement;
                type = nameStatement.type;
                name = nameStatement.name;
                values = new HashMap<String, String>();
            }
            else if (statement instanceof ValueStatement)
            {
                ValueStatement valueStatement = (ValueStatement) statement;
                values.put(valueStatement.key, valueStatement.value);
            }
        }
        if (type != null)
        {
            buildObject(scales, songs, type, name, values);
        }
        return songs;
    }

    private static void buildObject(Map<String, Scale> scales, Map<String, Song> songs, String type, String name,
            Map<String, String> values)
    {
        if (type.equals("scale"))
        {
            String middleValue = values.get("middle");
            String stepsValue = values.get("steps");
            int middle = Integer.parseInt(middleValue);
            String[] stepsArray = stepsValue.split(",");
            int[] steps = new int[stepsArray.length];
            for (int i = 0; i < steps.length; i++)
            {
                steps[i] = Integer.parseInt(stepsArray[i]);
            }
            scales.put(name, new Scale(middle, steps));
        }
        else if (type.equals("song"))
        {
            String scaleValue = values.get("scale");
            String notesValue = values.get("notes");
            String startValue = values.get("start");
            String bottomValue = values.get("bottom");
            String beatsValue = values.get("beats");
            if (!scales.containsKey(scaleValue))
            {
                throw new IllegalArgumentException("unrecognized scale: " + scaleValue);
            }
            Scale scale = scales.get(scaleValue);
            String[] notesArray = notesValue.split(",");
            Note[] notes = new Note[notesArray.length];
            for (int i = 0; i < notes.length; i++)
            {
                notes[i] = Note.valueOf(notesArray[i]);
            }
            Note start = Note.valueOf(startValue);
            Fraction bottom = Fraction.valueOf(bottomValue);
            Fraction beats = Fraction.valueOf(beatsValue);
            songs.put(name, new Song(scale, notes, start, bottom, beats));
        }
        else
        {
            throw new IllegalArgumentException("unrecognized type: " + type);
        }
    }

    private static Statement parseStatement(String line)
    {
        line = normalizeLine(line);
        if (line == null)
        {
            return null;
        }
        else if (line.matches("^[a-z]+ .*$"))
        {
            String[] parts = line.split(" ", 2);
            return new NameStatement(parts[0], parts[1]);
        }
        else if (line.matches("^[a-z]+:.*$"))
        {
            String[] parts = line.split(":", 2);
            return new ValueStatement(parts[0], parts[1]);
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    static String normalizeLine(String line)
    {
        line = line.replaceAll("//.*$", "");
        line = line.replaceAll("[\0- ]+", " ");
        line = line.replaceAll(" ?: ?", ":");
        line = line.replaceAll(" ?, ?", ",");
        line = line.toLowerCase().trim();
        return line.length() == 0 ? null : line;
    }
}
