package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        String begin = "<title>";
        String end = "</title>";
        int startIndex = _htmlCode.indexOf(begin);
        int endIndex = _htmlCode.indexOf(end);

        if (startIndex != -1 && endIndex > startIndex + begin.length()) {
            String titleStr = _htmlCode.substring(startIndex + begin.length(),endIndex);
            int specialCharIndex = titleStr.indexOf(" |");
            if (specialCharIndex != -1){
                return titleStr.substring(0,specialCharIndex);
            }
            return titleStr;
        }

        return "Title not found!";

    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        String begin = "\"articleBody\": \"";
        int startIndex = _htmlCode.indexOf(begin);
        int endIndex = _htmlCode.indexOf("\",\"", startIndex+begin.length());
        if (startIndex != -1 && endIndex >= startIndex + begin.length()){
            String content = _htmlCode.substring(startIndex + begin.length(),endIndex);
            content = content.toLowerCase().trim();
            return content;
        }

        return "Content not found!";
    }

}
