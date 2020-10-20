package com.icebuf.jetpackex.sample.plugins;

import io.noties.prism4j.annotations.PrismBundle;

/**
 * @author IceTang
 * @version 1.0
 * Data: 2020/9/29
 * E-mail：bflyff@hotmail.com
 *
 * 支持高亮显示的语言：
 * [ "brainfuck", "c", "clike", "clojure", "cpp", "csharp", "css", "css-extras"
 * "dart", "git", "go", "groovy", "java", "javascript", "json", "kotlin"
 * "latex", "makefile", "markdown", "markup", "python", "scala", "sql"
 * "swift", "yaml" ]
 */
@PrismBundle(
        include = {"c", "cpp", "csharp", "css", "dart", "git", "go", "groovy",
                "java", "javascript", "json", "kotlin", "makefile", "markdown",
                "python", "scala", "sql", "swift", "yaml"}
)
public class AllGrammarLocator  {

}
