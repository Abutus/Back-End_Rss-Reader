package rssreader;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import rssreader.dto.RssFeedDto;
import rssreader.rssstores.impl.ArrayListRssFeedStore;
import rssreader.rssstores.RssFeedStore;

public class RssFeedSaxHandler extends DefaultHandler{
    private RssFeedStore store = new ArrayListRssFeedStore();
    private RssFeedDto currentRssFeed;
    private String currentElement;
    private StringBuffer currentCharacters;

    public RssFeedSaxHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("source")) {
            currentRssFeed = new RssFeedDto();
        }

        currentElement = qName;
        currentCharacters = new StringBuffer();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(currentRssFeed != null && currentElement != null){
            if(currentElement.equalsIgnoreCase("title") ||
                    currentElement.equalsIgnoreCase("link")){
                currentCharacters.append(ch, start, length);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("source")){
            store.add(currentRssFeed);
            return;
        }
        if(currentElement != null && currentCharacters.length() > 0){
            if(qName.equalsIgnoreCase("title"))
                currentRssFeed.setTitle(currentCharacters.toString());
            if(qName.equalsIgnoreCase("link"))
                currentRssFeed.setLink(currentCharacters.toString());
        }
    }

    public RssFeedStore getStore(){
        return store;
    }
}
