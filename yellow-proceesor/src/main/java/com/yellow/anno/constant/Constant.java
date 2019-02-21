package com.yellow.anno.constant;

public class Constant {
    
    public static enum ProjectType {
        B, C
        ;
    }
    
    
    public enum Document {
        FileName("DocumentTemp", "Document"),
        BImport("import com.yellow.anno.MyId;", "import org.springframework.data.annotation.Id;"),
        BAnnotation("@MyId", "@Id"),
        CImport("import com.yellow.anno.MyId;", "import org.jongo.marshall.jackson.oid.MongoObjectId;"),
        CAnnotation("@MyId", "@MongoObjectId"),
        ;
        
        /**
         * 原始的内容
         */
        private String before;
        
        /**
         * 预期替换后的内容
         */
        private String after;
        
        private Document(String str1, String str2) {
            before = str1;
            after = str2;
        }

        public String getBefore() {
            return before;
        }


        public String getAfter() {
            return after;
        }

    }
}
