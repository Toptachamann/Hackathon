package test.back.code;


import javax.swing.text.View;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
//import javafx.utils;


public class EncodedSequenceMatcher {

    private class Pair {
        public Integer first;
        public Integer second;

        public Pair(Integer a, Integer b) {
            first = a;
            second = b;
        }
    }

    List<Pair> first = new ArrayList<>();
    List<Pair> second = new ArrayList<>();
    int start1;
    int start2;
    ArrayList<Pair> ans;

    public EncodedSequenceMatcher(List<Integer> _first, List<Integer> _second) {
        for (int i = 0; i < _first.size(); ++i) {
            first.add(new Pair(_first.get(i), i));
        }
        for (int i = 0; i < _second.size(); ++i) {
            second.add(new Pair(_second.get(i), i));
        }
    }

    void merge(List<EqualsBlock> blocks){
        blocks.sort(Comparator.comparingInt((EqualsBlock block) -> block.start2));
        for (int i = 0; i + 1 < blocks.size(); i++) {
            EqualsBlock first = blocks.get(i);
            EqualsBlock second = blocks.get(i + 1);
            if (!first.overlapsWith(second)) {
                if (first.finish2 > second.start2 - 15) {
                    first.finish1 += second.lengthSecond();
                    first.finish2 = second.finish2;
                    blocks.remove(i + 1);
                    i--;
                }
            }
        }
    }

    protected int longestCommonSubsequence(List<Pair> l1, List<Pair> l2) {
        int N = l1.size();
        int M = l2.size();
        int[][] dp = new int[N + 1][M + 1];
        Pair[][] p = new Pair[N + 1][M + 1];
        int mx = -1;
        int n = 0, m = 0;
        for (int i = 1; i <= N; ++i) {
            for (int j = 1; j <= M; ++j) {
                if (l1.get(i - 1).first == l2.get(j - 1).first) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    p[i][j] = new Pair(i - 1, j - 1);
                } else {
                    if (dp[i - 1][j] > dp[i][j - 1]) {
                        dp[i][j] = 0;//dp[i - 1][j];
                        p[i][j] = new Pair(-1, -1);
                    } else {
                        dp[i][j] = 0;//dp[i][j - 1];
                        p[i][j] = new Pair(-1, -1);
                    }
                }
                if (dp[i][j] > mx) {
                    mx = dp[i][j];
                    n = i;
                    m = j;
                }
            }
        }
        N = n;
        M = m;
        ans = new ArrayList<>();
        while (N > 0 && M > 0) {
            int new_n = p[N][M].first;
            int new_m = p[N][M].second;
            if (new_n == N) {
                M = new_m;
            } else if (new_m == M) {
                N = new_n;
            } else {
                ans.add(new Pair(N - 1, M - 1));
                N = new_n;
                M = new_m;
            }
        }
        Collections.reverse(ans);
        if (ans.size() > mx) ans.remove(0);
        if (mx != 0) {
            start1 = ans.get(0).first;
            start2 = ans.get(0).second;
        } else {
            start1 = -1;
            start2 = -1;
        }
        return mx;
    }

    public List<EqualsBlock> getRate() {
        List<EqualsBlock> res = new ArrayList<>();
        int x;
        do {
            x = longestCommonSubsequence(first, second);
            if (x < 15) {
                break;
            }
            int finish2 = start2 + x;
            res.add(new EqualsBlock(first.get(start1).second, first.get(start1).second + x - 1,
                    second.get(start2).second, second.get(start2).second + x - 1));
            for (int i = start2; i < finish2; ++i) {
                second.set(i, new Pair(-1, -1));
            }
        } while (x > 0);

//        merge(res);

        return res;
    }


}
