/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package silly.metrics;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A silly metrics keyword counter.
 * <p>
 * This tool counts all Java keywords in .java files contained in any given path. For example:
 * <code><pre>
 * $ java silly.metrics.KeywordCounter /path/to/java/project
 * </pre></code>
 * ... and observe the number of Java keywords used in your project. For instance, running this on <a href="http://www.jooq.org">http://www.jooq.org</a>:
 * 
 * <pre>
 * Silly metrics: Keywords
 * =======================
 * Enter a path (C:\Users\Lukas\workspace\silly-metrics):
 * C:\Users\Lukas\git\jOOQ\jOOQ
 *
 *
 * Keyword      Count
 * public       8127
 * return       6801
 * final        6608
 * import       5938
 * static       3903
 * new          3110
 * extends      2111
 * int          1822
 * throws       1756
 * void         1707
 * if           1661
 * this         1464
 * private      1347
 * class        1239
 * case         841
 * else         839
 * package      711
 * boolean      506
 * throw        495
 * for          421
 * long         404
 * true         384
 * byte         345
 * interface    337
 * false        332
 * protected    293
 * super        265
 * break        200
 * try          149
 * switch       146
 * implements   139
 * catch        127
 * default      112
 * instanceof   107
 * char         96
 * short        91
 * abstract     54
 * double       43
 * transient    42
 * finally      34
 * float        34
 * enum         25
 * while        23
 * continue     12
 * synchronized 8
 * volatile     6
 * do           1
 * </pre>
 */
public class KeywordCounter {

    static final Map<String, Integer> RESULT        = new HashMap<String, Integer>();
    static Set<String>                JAVA_KEYWORDS = unmodifiableSet(new HashSet<String>(asList(
        "abstract",
        "assert",
        "boolean",
        "break",
        "byte",
        "case",
        "catch",
        "char",
        "class",
        "const",
        "continue",
        "default",
        "double",
        "do",
        "else",
        "enum",
        "extends",
        "false",
        "final",
        "finally",
        "float",
        "for",
        "goto",
        "if",
        "implements",
        "import",
        "instanceof",
        "interface",
        "int",
        "long",
        "native",
        "new",
        "package",
        "private",
        "protected",
        "public",
        "return",
        "short",
        "static",
        "strictfp",
        "super",
        "switch",
        "synchronized",
        "this",
        "throw",
        "throws",
        "transient",
        "true",
        "try",
        "void",
        "volatile",
        "while")));

    public static void main(String[] args) throws Exception {
        String path = null;

        System.out.println("Silly metrics: Keywords");
        System.out.println("=======================");

        if (args.length > 0) {
            path = args[0];
        }
        else {
            System.out.println("Enter a path (" + new File(".").getCanonicalPath() + "):");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            path = br.readLine();

            if (path == null || path.equals("")) {
                path = ".";
            }
        }

        recurse(new File(path));
        List<String> keywords = new ArrayList<String>(RESULT.keySet());
        Collections.sort(keywords, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -RESULT.get(o1).compareTo(RESULT.get(o2));
            }
        });

        System.out.println();
        System.out.println();
        System.out.println(String.format("%1$-12s %2$s", "Keyword", "Count"));
        for (String keyword : keywords) {
            System.out.println(String.format("%1$-12s %2$s", keyword, RESULT.get(keyword)));
        }
    }

    private static void recurse(File file) throws Exception {
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (File f : files) {
                recurse(f);
            }
        }
        else if (file.getName().endsWith(".java")) {
            RandomAccessFile f = new RandomAccessFile(file, "r");
            byte[] contents = new byte[(int) f.length()];
            f.readFully(contents);
            f.close();
            String content = new String(contents);

            char c;
            int i = 0;
            int state = CODE;
            StringBuilder sb = new StringBuilder();

            loop: while (i < content.length()) {
                c = content.charAt(i);

                switch (state) {
                    case CODE:
                        if (peek(content, i, "/*")) {
                            state = MULTI_LINE_COMMENT;
                            i += 2;
                            continue loop;
                        }

                        else if (peek(content, i, "//")) {
                            state = SINGLE_LINE_COMMENT;
                            i += 2;
                            continue loop;
                        }

                        else if (peek(content, i, "\"")) {
                            state = STRING_LITERAL;
                            i += 1;
                            continue loop;
                        }

                        else if (Character.isJavaIdentifierPart(c)) {
                            sb.append(c);
                        }

                        else {
                            if (sb.length() > 0) {
                                String s = sb.toString();

                                if (JAVA_KEYWORDS.contains(s)) {
                                    Integer count = RESULT.get(s);

                                    if (count == null) {
                                        count = 0;
                                    }

                                    RESULT.put(s, count + 1);
                                }

                                sb = new StringBuilder();
                            }
                        }

                        break;

                    case SINGLE_LINE_COMMENT:
                        if (c == '\n' || c == '\r') {
                            state = CODE;
                            continue loop;
                        }

                        break;

                    case MULTI_LINE_COMMENT:
                        if (peek(content, i, "*/")) {
                            state = CODE;
                            i += 2;
                            continue loop;
                        }

                        break;

                    case STRING_LITERAL:
                        if (peek(content, i, "\"")) {
                            state = CODE;
                            i += 1;
                            continue loop;
                        }
                }

                i++;
            }
        }
    }

    private static boolean peek(String string, int position, String content) {
        if (string.length() < position + content.length()) {
            return false;
        }

        for (int i = 0; i < content.length(); i++) {
            if (string.charAt(position + i) != content.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    private static final int CODE = 0;
    private static final int MULTI_LINE_COMMENT = 1;
    private static final int SINGLE_LINE_COMMENT = 2;
    private static final int STRING_LITERAL = 3;
}
