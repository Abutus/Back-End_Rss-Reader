package rssreader;

import rssreader.dto.RssDto;
import rssreader.rssstores.impl.ArrayListRssStore;
import rssreader.rssstores.RssStore;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RssSaxHandler extends DefaultHandler {

    RssStore store = new ArrayListRssStore();
    RssDto currentRss;
    String currentElement;
    StringBuffer currentCharacters;

    public RssSaxHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("item")) {
            currentRss = new RssDto();
        }

        currentElement = qName;
        currentCharacters = new StringBuffer();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(currentRss != null && currentElement != null){
            if(currentElement.equalsIgnoreCase("title") ||
                    currentElement.equalsIgnoreCase("link") ||
                    currentElement.equalsIgnoreCase("description") ||
                    currentElement.equalsIgnoreCase("guid") ||
                    currentElement.equalsIgnoreCase("pubDate")){
                currentCharacters.append(ch, start, length);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("item")){
            store.add(currentRss);
        }
        if(currentElement != null && currentCharacters.length() > 0){
            if(qName.equalsIgnoreCase("title"))
                currentRss.setTitle(currentCharacters.toString());
            if(qName.equalsIgnoreCase("link"))
                currentRss.setLink(currentCharacters.toString());
            if(qName.equalsIgnoreCase("description"))
                currentRss.setDescription(currentCharacters.toString());
            if(qName.equalsIgnoreCase("guid"))
                currentRss.setGuid(currentCharacters.toString());
            if(qName.equalsIgnoreCase("pubDate"))
                currentRss.setPubDate(currentCharacters.toString());
        }
    }

    public RssStore getStore(){
        return store;
    }
}
