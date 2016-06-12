package cn.weekdragon.superdic.util;

import android.util.Xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.weekdragon.superdic.bean.PosAndAcc;
import cn.weekdragon.superdic.bean.PsAndPron;
import cn.weekdragon.superdic.bean.Sent;
import cn.weekdragon.superdic.bean.Word;

public class SolverXml {
    private static Word word;
    private static List<PsAndPron> pslList;
    private static PsAndPron andPron;
    private static List<PosAndAcc> posList;
    private static PosAndAcc andAcc;
    private static List<Sent> sentList;
    private static Sent sent;
    private static MyHandler handler;
    private static String tag;

    public static Word jiexi(InputStream input) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        // XMLReader reader = parser.getXMLReader();
        // reader.setContentHandler(new MyHandler());
        handler = new MyHandler();
        parser.parse(input, handler);
        return word;
    }

    public static class MyHandler extends DefaultHandler {
        @Override
        public void startDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.startDocument();
            word = new Word();
            pslList = new ArrayList<PsAndPron>();
            posList = new ArrayList<PosAndAcc>();
            sentList = new ArrayList<Sent>();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            // TODO Auto-generated method stub
            super.startElement(uri, localName, qName, attributes);
            if (localName.equals("ps")) {
                andPron = new PsAndPron();
            } else if (localName.equals("pos")) {
                andAcc = new PosAndAcc();
            } else if (localName.equals("sent")) {
                sent = new Sent();
            }
            tag = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            // TODO Auto-generated method stub
            super.characters(ch, start, length);
            if (tag != null) {
                String data = new String(ch, start, length);
                if (tag.equals("key")) {
                    word.setKey(data);
                } else if (tag.equals("ps")) {
                    andPron.setPs(data);
                } else if (tag.equals("pron")) {
                    andPron.setPron(data);
                } else if (tag.equals("pos")) {
                    andAcc.setPos(data);
                } else if (tag.equals("acceptation")) {
                    andAcc.setAcceptation(data);
                } else if (tag.equals("orig")) {
                    sent.setOrig(data);
                } else if (tag.equals("trans")) {
                    sent.setTrans(data);
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // TODO Auto-generated method stub
            super.endElement(uri, localName, qName);
            if (localName.equals("pron")) {
                pslList.add(andPron);
                andPron = null;
            } else if (localName.equals("acceptation")) {
                posList.add(andAcc);
                andAcc = null;
            } else if (localName.equals("sent")) {
                sentList.add(sent);
                sent = null;
            } else if (localName.equals("dict")) {
                word.setPsandpron(pslList);
                word.setPosandacc(posList);
                word.setSent(sentList);
            }
            tag = null;
        }

        @Override
        public void endDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.endDocument();
        }

    }

    /**
     * Pull解析
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static Word jiexi2(InputStream input) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(input, "UTF-8");
        int envent = parser.getEventType();
        while (envent != parser.END_DOCUMENT) {
            // System.out.println("start====" + parser.getName());
            switch (envent) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("dict")) {
                        word = new Word();
                        pslList = new ArrayList<PsAndPron>();
                        posList = new ArrayList<PosAndAcc>();
                        sentList = new ArrayList<Sent>();
                    }
                    if (word != null) {
                        if (parser.getName().equals("key")) {
                            String data = parser.nextText();
                            // System.out.println("data1---" + data);
                            word.setKey(data);
                        } else if (parser.getName().equals("ps")) { // PS节点
                            String data = parser.nextText();
                            // System.out.println("data2---" + data);
                            andPron = new PsAndPron();
                            andPron.setPs(data);
                        } else if (parser.getName().equals("pron")) {
                            if (andPron != null) {
                                String data = parser.nextText();
                                // System.out.println("data3---" + data);
                                andPron.setPron(data);
                                pslList.add(andPron);
                                andPron = null;
                            }
                        } else if (parser.getName().equals("pos")) { // POS节点
                            andAcc = new PosAndAcc();
                            String data = parser.nextText();
                            // System.out.println("data4---" + data);
                            andAcc.setPos(data);
                        } else if (parser.getName().equals("acceptation")) {
                            if (andAcc != null) {
                                String data = parser.nextText();
                                // System.out.println("data5---" + data);
                                andAcc.setAcceptation(data);
                                posList.add(andAcc);
                                andAcc = null;
                            }
                        } else if (parser.getName().equals("sent")) { // Sent节点
                            sent = new Sent();
                        } else if (parser.getName().equals("orig")) {
                            if (sent != null) {
                                String data = parser.nextText();
                                // System.out.println("data6---" + data);
                                sent.setOrig(data);
                            }
                        } else if (parser.getName().equals("trans")) {
                            if (sent != null) {
                                String data = parser.nextText();
                                // System.out.println("data7---" + data);
                                sent.setTrans(data);
                            }
                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    // System.out.println("-------end_TAG==" + parser.getName());
                    if (parser.getName().equals("pron")) {
                        pslList.add(andPron);
                        andPron = null;
                    } else if (parser.getName().equals("acceptation")) {
                        posList.add(andAcc);
                        andAcc = null;
                    } else if (parser.getName().equals("sent")) {
                        sentList.add(sent);
                        sent = null;
                    } else if ("dict".equals(parser.getName())) {
                        word.setPsandpron(pslList);
                        word.setPosandacc(posList);
                        word.setSent(sentList);
                    }
                    break;
            }
            envent = parser.next();
        }
        input.close();
        return word;
    }

}
