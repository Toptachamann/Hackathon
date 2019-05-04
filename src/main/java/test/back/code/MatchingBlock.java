package test.back.code;

public class MatchingBlock {
    public String original;
    public String plag;


    public MatchingBlock(String original, String plag) {
        this.original = original;
        this.plag = plag;
    }

    @Override
    public String toString() {
        return String.format("Original:\n %s\nPlagiarism:\n %s\n", original, plag);
    }
}
