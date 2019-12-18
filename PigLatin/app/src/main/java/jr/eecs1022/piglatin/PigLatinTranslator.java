package jr.eecs1022.piglatin;

import java.util.StringTokenizer;

public class PigLatinTranslator
{
    private String english;
    private String pig;

    public PigLatinTranslator()
    {
        this.setEnglish("");
    }

    public PigLatinTranslator(String text)
    {
        this.setEnglish(text);
    }

    public String getEnglish()
    {
        return this.english;
    }

    public void setEnglish(String text)
    {
        this.english = text.toLowerCase();
        this.translate();
    }

    public String getPig()
    {
        return this.pig;
    }

    // Translate the state to PigLatin
    public void translate()
    {
        StringTokenizer st = new StringTokenizer(this.english);
        String result = "";
        while (st.hasMoreTokens())
        {
            String word = st.nextToken();
            String pig = this.translateWord(word);
            if (result.length() == 0)
            {
                result = pig;
            }
            else
            {
                result = result + " " + pig;
            }
        }
        this.pig = result;
    }

    // Translate the given word to PigLatin
    // and return the translation
    private String translateWord(String word)
    {
        String result = word.replaceAll("[^A-Za-z]+","");

        if (result == "") return "";

        result.toLowerCase();
        boolean containsVowels = false;
        for (int i = 0; i < result.length(); i++)
            if ("aeiou".indexOf(result.charAt(i)) >= 0) containsVowels = true;
        
        if (containsVowels && "aeiou".indexOf(result.charAt(0)) >= 0) result += "way";

        else if (!containsVowels) result += "ay";

        else result = result.substring(1,result.length()) + result.substring(0,1) + "ay";

        return result;
    }


}