package uob.oop;

public class NLP {
    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    public static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();
        _content = _content.toLowerCase();
        char[] chars = _content.toCharArray();
        for (char c : chars){
            if ((c >= 'a'&& c <= 'z')||(c >='A'&& c <='Z') || (c>='0' && c<='9') || c ==' '){
                sbContent.append(c);
            }
        }

        return sbContent.toString().trim();
    }

    /***
     * Text lemmatization. Delete 'ing', 'ed', 'es' and 's' from the end of the word.
     * @param _content Text that need to be lemmatized.
     * @return Lemmatized text.
     */
    public static String textLemmatization(String _content) {
        StringBuilder sbContent = new StringBuilder();
        String[] strings = _content.split(" ");
        for (String s : strings){
            if (s.endsWith("ing")){
                sbContent.append(s, 0, s.lastIndexOf("ing"));
            }else if (s.endsWith("ed")){
                sbContent.append(s, 0, s.lastIndexOf("ed"));
            } else if (s.endsWith("es")) {
                sbContent.append(s, 0, s.lastIndexOf("es"));
            } else if (s.endsWith("s")) {
                sbContent.append(s, 0, s.lastIndexOf("s"));
            }else {
                sbContent.append(s);
            }
            sbContent.append(" ");
        }

        return sbContent.toString().trim();
    }

    /***
     * Remove stop-words from the text.
     * @param _content The original text.
     * @param _stopWords An array that contains stop-words.
     * @return Modified text.
     */
    public static String removeStopWords(String _content, String[] _stopWords) {
        StringBuilder sbConent = new StringBuilder();
        String[] strings = _content.split(" ");
        for (String s : strings){
            boolean flag = true;
            for (String stop:_stopWords){
                if (s.equals(stop)){
                    flag = false;
                    break;
                }
            }
            if (flag){
                sbConent.append(s);
                sbConent.append(" ");
            }
        }

        return sbConent.toString().trim();
    }

}
