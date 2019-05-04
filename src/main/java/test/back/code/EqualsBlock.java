package test.back.code;

public class EqualsBlock {
    public int start1;
    public int start2;
    public int finish1;
    public int finish2;

    public EqualsBlock(int s1, int f1, int s2, int f2) {
        start1 = s1;
        start2 = s2;
        finish1 = f1;
        finish2 = f2;
    }

    boolean overlapsWith(EqualsBlock block) {
        return !overlaps(start1, finish1, block.start1, block.finish1) && !overlaps(start2, finish2, block.start2, block.finish2);
    }

    private boolean overlaps(int x1, int y1, int x2, int y2) {
        return Math.max(x1, x2) > Math.min(y1, y2);
    }

    public int lengthFirst() {
        return finish1 - start1 + 1;
    }

    public int lengthSecond() {
        return finish2 - start2 + 1;
    }


    @Override
    public String toString() {
        return String.format("{(%d, %d), (%d, %d)}", start1, finish1, start2, finish2);
    }
}
