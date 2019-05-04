package test.back.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import test.back.code.MatchingBlock;
import test.back.code.SimilarityFinder;

import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String procces(@RequestBody String str) {
        JSONObject obj = new JSONObject(str);

        String extension1 = obj.getString("ext1");
        String code1 = obj.getString("code1");

        JSONArray ext2 = obj.getJSONArray("ext2");
        JSONArray code2 = obj.getJSONArray("code2");
        for (int i = 0; i < ext2.length(); ++i) {
            String ext2_i = ext2.getString(i);
            String code2_i = code2.getString(i);
            System.out.println(ext2_i);

            SimilarityFinder finder = new SimilarityFinder();
            List<MatchingBlock> ll = new ArrayList<>();
            double coef = finder.findSimilarity(code1, code2_i, SimilarityFinder.Lang.JAVA, ll);
        }



//        System.out.println(code1);
//        System.out.println("---------------------------");
//        System.out.println(code2);

        JSONObject resp = new JSONObject();
        List<String> s1 = new ArrayList<>();
        List<String> s2 = new ArrayList<>();
        s1.add("test1");
        s2.add("test2");
        s1.add("");
        s2.add("lol");
        s1.add("kek");
        s2.add("kekkekekeke");

        resp.put("test", new JSONArray().put(s1));
        resp.put("plag", new JSONArray().put(s2));

        return resp.toString();
    }
}