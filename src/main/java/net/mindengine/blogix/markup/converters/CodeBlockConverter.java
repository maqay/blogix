/*******************************************************************************
* Copyright 2012 Ivan Shubin http://mindengine.net
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
* 
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
******************************************************************************/
package net.mindengine.blogix.markup.converters;

import org.apache.commons.lang3.StringEscapeUtils;

public class CodeBlockConverter implements Converter {

    @Override
    public String convert(String definition, String text) {
        StringBuffer buffer = new StringBuffer("<code class=\"block\"");
        buffer.append(dataLanguageFromDefinition(definition));
        buffer.append(">");
        buffer.append(StringEscapeUtils.escapeHtml4(text));
        buffer.append("</code>");
        return buffer.toString();
    }

    private String dataLanguageFromDefinition(String definition) {
        if (definition != null && definition.length() > 2) {
            String language = definition.substring(2).trim();
            if (!language.isEmpty()) {
                return String.format(" data-language=\"%s\"", StringEscapeUtils.escapeHtml4(language));
            }
        }
        return "";
    }
}