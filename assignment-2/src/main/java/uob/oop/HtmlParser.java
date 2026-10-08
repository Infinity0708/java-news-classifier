package uob.oop;

public class HtmlParser {
    /***
     * Extract the title of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the title if it's been found. Otherwise, return "Title not found!".
     */
    public static String getNewsTitle(String _htmlCode) {
        String titleTagOpen = "<title>";
        String titleTagClose = "</title>";

        int titleTagIndex = _htmlCode.indexOf(titleTagOpen);
        int titleStart = titleTagIndex < 0 ? -1 : titleTagIndex + titleTagOpen.length();
        int titleEnd = _htmlCode.indexOf(titleTagClose);

        if (titleStart != -1 && titleEnd != -1 && titleEnd > titleStart) {
            String strFullTitle = _htmlCode.substring(titleStart, titleEnd);
            int separator = strFullTitle.indexOf(" |");
            return separator < 0 ? strFullTitle : strFullTitle.substring(0, separator);
        }

        return "Title not found!";
    }

    /***
     * Extract the content of the news from the _htmlCode.
     * @param _htmlCode Contains the full HTML string from a specific news. E.g. 01.htm.
     * @return Return the content if it's been found. Otherwise, return "Content not found!".
     */
    public static String getNewsContent(String _htmlCode) {
        String contentTagOpen = "\"articleBody\": \"";
        String contentTagClose = " \",\"mainEntityOfPage\":";

        int contentTagIndex = _htmlCode.indexOf(contentTagOpen);
        int contentStart = contentTagIndex < 0 ? -1 : contentTagIndex + contentTagOpen.length();
        int contentEnd = _htmlCode.indexOf(contentTagClose);

        if (contentStart != -1 && contentEnd != -1 && contentEnd > contentStart) {
            return _htmlCode.substring(contentStart, contentEnd).toLowerCase();
        }

        return "Content not found!";
    }

    public static NewsArticles.DataType getDataType(String _htmlCode) {
        String typeTagOpen = "<datatype>";
        String typeTagClose = "</datatype>";

        int typeTagIndex = _htmlCode.indexOf(typeTagOpen);
        int typeStart = typeTagIndex < 0 ? -1 : typeTagIndex + typeTagOpen.length();
        int typeEnd = _htmlCode.indexOf(typeTagClose);

        if (typeStart != -1 && typeEnd != -1 && typeEnd > typeStart) {
            String datatype = _htmlCode.substring(typeStart, typeEnd);
            if (datatype.equals("Training")) return NewsArticles.DataType.Training;
            if (datatype.equals("Testing")) return NewsArticles.DataType.Testing;
        }

        return NewsArticles.DataType.Testing;
    }

    public static String getLabel (String _htmlCode) {
        String labelTagOpen = "<label>";
        String labelTagClose = "</label>";

        int labelTagIndex = _htmlCode.indexOf(labelTagOpen);
        int labelStart = labelTagIndex < 0 ? -1 : labelTagIndex + labelTagOpen.length();
        int labelEnd = _htmlCode.indexOf(labelTagClose);

        if (labelStart != -1 && labelEnd != -1 && labelEnd > labelStart) {
            return _htmlCode.substring(labelStart, labelEnd);
        }
        return "-1";
    }

}
