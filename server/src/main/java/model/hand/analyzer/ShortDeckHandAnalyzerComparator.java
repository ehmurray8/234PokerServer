package model.hand.analyzer;

public class ShortDeckHandAnalyzerComparator extends HandAnalyzerComparator {

    @Override
    public int compare(HandAnalyzer ha1, HandAnalyzer ha2) {
        handAnalyzer = ha1;
        otherHandAnalyzer = ha2;
        int value = handAnalyzer.getTopRank().getShortDeckStrength() - otherHandAnalyzer.getTopRank().getShortDeckStrength();
        return compareStrengths(value);
    }
}
