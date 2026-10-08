package uk.co.caprica.vlcj.factory;

import uk.co.caprica.vlcj.parser.Parser;

public final class ParserApi extends BaseApi {

    ParserApi(MediaPlayerFactory factory) {
        super(factory);
    }

    public Parser newParser() {
        return new Parser(libvlcInstance);
    }

    public Parser newParser(long timeout) {
        return new Parser(libvlcInstance, timeout);
    }

    public Parser newParser(int maxParserThreads, int maxThumbnailerThreads) {
        return new Parser(libvlcInstance, maxParserThreads, maxThumbnailerThreads);
    }

    public Parser newParser(long timeout, int maxParserThreads, int maxThumbnailerThreads) {
        return new Parser(libvlcInstance, timeout, maxParserThreads, maxThumbnailerThreads);
    }
}
