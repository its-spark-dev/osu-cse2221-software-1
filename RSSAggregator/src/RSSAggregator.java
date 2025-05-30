import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to convert multiple XML RSS (version 2.0) feeds into corresponding
 * HTML pages and then generate an index HTML page with links to the feed pages.
 *
 * The input XML document has the following general structure:
 *
 * <feeds title="Title for index page">
 * <feed url="the feed source URL" name="name of feed for index page" file="name
 * of HTML file for feed" /> <feed url="..." name="..." file="..." /> ...
 * </feeds>
 *
 * @author S. Park
 *
 */
public final class RSSAggregator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RSSAggregator() {
    }

    /**
     * Outputs the "opening" tags in the generated HTML file. These are the
     * expected elements generated by this method.
     *
     * @param channel
     *            the channel element XMLTree
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the root of channel is a <channel> tag] and out.is_open
     * @ensures out.content = #out.content * [the HTML "opening" tags]
     */
    private static void outputHeader(XMLTree channel, SimpleWriter out) {
        assert channel != null : "Violation of: channel is not null";
        assert out != null : "Violation of: out is not null";
        assert channel.isTag() && channel.label().equals("channel")
                : "Violation of: the label root of channel is a <channel> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        /*
         * Extract title, link, description from channel
         */
        String title = "Empty Title";
        int titleIndex = getChildElement(channel, "title");
        if (titleIndex != -1 && channel.child(titleIndex).numberOfChildren() > 0) {
            title = channel.child(titleIndex).child(0).label();
        }
        String link = "";
        int linkIndex = getChildElement(channel, "link");
        if (linkIndex != -1 && channel.child(linkIndex).numberOfChildren() > 0) {
            link = channel.child(linkIndex).child(0).label();
        }
        String description = "No description";
        int descIndex = getChildElement(channel, "description");
        if (descIndex != -1 && channel.child(descIndex).numberOfChildren() > 0) {
            description = channel.child(descIndex).child(0).label();
        }

        /*
         * Output HTML header
         */
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1><a href=\"" + link + "\">" + title + "</a></h1>");
        out.println("<p>" + description + "</p>");
        out.println("<table border=\"1\">");
        out.println("<tr>");
        out.println("<th>Date</th>");
        out.println("<th>Source</th>");
        out.println("<th>News</th>");
        out.println("</tr>");
    }

    /**
     * Outputs the "closing" tags in the generated HTML file.
     *
     * @param out
     *            the output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.content = #out.content * [the HTML "closing" tags]
     */
    private static void outputFooter(SimpleWriter out) {
        assert out != null : "Violation of: out is not null";
        assert out.isOpen() : "Violation of: out.is_open";

        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Finds the first occurrence of the given tag among the children of the
     * given XMLTree and returns its index; returns -1 if not found.
     *
     * @param xml
     *            the XMLTree to search
     * @param tag
     *            the tag to look for
     * @return the index of the first child of type tag of the XMLTree or -1 if
     *         not found
     * @requires [the label of the root of xml is a tag]
     * @ensures getChildElement = [the index of the first child of type tag of
     *          the XMLTree or -1 if not found]
     */
    private static int getChildElement(XMLTree xml, String tag) {
        assert xml != null : "Violation of: xml is not null";
        assert tag != null : "Violation of: tag is not null";
        assert xml.isTag() : "Violation of: the label root of xml is a tag";

        int index = -1;
        for (int i = 0; i < xml.numberOfChildren(); i++) {
            XMLTree child = xml.child(i);
            if (child.isTag() && child.label().equals(tag)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Processes one news item and outputs one table row. The row contains three
     * cells: publication date, source, and title (or description).
     *
     * @param item
     *            the news item
     * @param out
     *            the output stream
     * @updates out.content
     * @requires [the label of the root of item is an <item> tag] and
     *           out.is_open
     * @ensures out.content = #out.content * [an HTML table row with publication
     *          date, source, and title of news item]
     */
    private static void processItem(XMLTree item, SimpleWriter out) {
        assert item != null : "Violation of: item is not null";
        assert out != null : "Violation of: out is not null";
        assert item.isTag() && item.label().equals("item")
                : "Violation of: the label root of item is an <item> tag";
        assert out.isOpen() : "Violation of: out.is_open";

        /*
         * Extract publication date
         */
        String date = "No date available";
        int pubDateIndex = getChildElement(item, "pubDate");
        if (pubDateIndex != -1 && item.child(pubDateIndex).numberOfChildren() > 0) {
            date = item.child(pubDateIndex).child(0).label();
        }

        /*
         * Extract source and its URL
         */
        String source = "No source available";
        String sourceUrl = "";
        int sourceIndex = getChildElement(item, "source");
        if (sourceIndex != -1) {
            XMLTree sourceNode = item.child(sourceIndex);
            sourceUrl = sourceNode.hasAttribute("url") ? sourceNode.attributeValue("url")
                    : "";
            if (sourceNode.numberOfChildren() > 0) {
                source = sourceNode.child(0).label();
            }
        }

        /*
         * Extract news title (or, if missing, description)
         */
        String newsText = "";
        int titleIndex = getChildElement(item, "title");
        if (titleIndex != -1 && item.child(titleIndex).numberOfChildren() > 0) {
            newsText = item.child(titleIndex).child(0).label().trim();
        }
        if (newsText.isEmpty()) {
            int descIndex = getChildElement(item, "description");
            if (descIndex != -1 && item.child(descIndex).numberOfChildren() > 0) {
                newsText = item.child(descIndex).child(0).label().trim();
            }
        }
        if (newsText.isEmpty()) {
            newsText = "No title available";
        }

        /*
         * Extract link for the news item
         */
        String link = "";
        int linkIndex = getChildElement(item, "link");
        if (linkIndex != -1 && item.child(linkIndex).numberOfChildren() > 0) {
            link = item.child(linkIndex).child(0).label().trim();
        }

        /*
         * Output table row
         */
        out.println("<tr>");
        out.println("<td>" + date + "</td>");
        if (!sourceUrl.isEmpty()) {
            out.println("<td><a href=\"" + sourceUrl + "\">" + source + "</a></td>");
        } else {
            out.println("<td>" + source + "</td>");
        }
        if (!link.isEmpty()) {
            out.println("<td><a href=\"" + link + "\">" + newsText + "</a></td>");
        } else {
            out.println("<td>" + newsText + "</td>");
        }
        out.println("</tr>");
    }

    /**
     * Processes one XML RSS (version 2.0) feed from a given URL converting it
     * into the corresponding HTML output file.
     *
     * @param url
     *            the URL of the RSS feed
     * @param file
     *            the name of the HTML output file
     * @param out
     *            the output stream to report progress or errors
     * @updates out.content
     * @requires out.is_open
     * @ensures <pre>
     * [reads RSS feed from url, saves HTML document with table of news items
     *   to file, appends to out.content any needed messages]
     * </pre>
     */
    private static void processFeed(String url, String file, SimpleWriter out) {
        out.println("Processing feed from URL: " + url);
        XMLTree rss = new XMLTree1(url);
        if (!rss.label().equals("rss") || !rss.hasAttribute("version")
                || !rss.attributeValue("version").equals("2.0")) {
            out.println("Error: Invalid RSS 2.0 feed from URL: " + url);
            return;
        }
        int channelIndex = getChildElement(rss, "channel");
        if (channelIndex == -1) {
            out.println("Error: No channel found in RSS feed from URL: " + url);
            return;
        }
        XMLTree channel = rss.child(channelIndex);

        SimpleWriter fileOut = new SimpleWriter1L(file);
        outputHeader(channel, fileOut);
        for (int i = 0; i < channel.numberOfChildren(); i++) {
            XMLTree child = channel.child(i);
            if (child.isTag() && child.label().equals("item")) {
                processItem(child, fileOut);
            }
        }
        outputFooter(fileOut);
        fileOut.close();
        out.println("Finished processing feed: " + url);
    }

    /**
     * Main method. It prompts the user for the name of an XML file containing
     * the list of RSS feeds and for the name of an output HTML file for the
     * index page. Then it processes each feed and generates the corresponding
     * HTML page, as well as an index page.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Prompt for the input XML file with the list of feeds
         */
        out.print("Enter the name of the XML file containing the list of RSS feeds: ");
        String feedsFileName = in.nextLine();
        XMLTree feedsXML = new XMLTree1(feedsFileName);

        if (!feedsXML.label().equals("feeds") || !feedsXML.hasAttribute("title")) {
            out.println("Error: Invalid feeds XML file.");
            in.close();
            out.close();
            return;
        }
        String indexTitle = feedsXML.attributeValue("title");

        /*
         * Prompt for the output HTML file name for the index page
         */
        out.print(
                "Enter the name of the output HTML file for the index page (without extension): ");
        String indexFileName = in.nextLine();
        if (!indexFileName.endsWith(".html")) {
            indexFileName = indexFileName + ".html";
        }
        SimpleWriter indexOut = new SimpleWriter1L(indexFileName);

        // Begin the index HTML page
        indexOut.println("<html>");
        indexOut.println("<head>");
        indexOut.println("<title>" + indexTitle + "</title>");
        indexOut.println("</head>");
        indexOut.println("<body>");
        indexOut.println("<h1>" + indexTitle + "</h1>");
        indexOut.println("<ul>");

        /*
         * Process each feed specified in the feeds XML file
         */
        for (int i = 0; i < feedsXML.numberOfChildren(); i++) {
            XMLTree feed = feedsXML.child(i);
            if (feed.isTag() && feed.label().equals("feed")) {
                /*
                 * Retrieve required attributes: url, name, and file
                 */
                String url = feed.attributeValue("url");
                String feedName = feed.attributeValue("name");
                String file = feed.attributeValue("file");

                out.println("Processing feed: " + feedName);
                processFeed(url, file, out);
                /*
                 * Add a link to this feed's HTML page in the index page
                 */
                indexOut.println(
                        "<li><a href=\"" + file + "\">" + feedName + "</a></li>");
            }
        }

        /*
         * Finish the index HTML page
         */
        indexOut.println("</ul>");
        indexOut.println("</body>");
        indexOut.println("</html>");

        /*
         * Close all streams
         */
        indexOut.close();
        in.close();
        out.close();
    }
}
