/*******************************************************************************
* Copyright 2013 Ivan Shubin http://mindengine.net
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
package net.mindengine.blogix.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;

import net.mindengine.blogix.Blogix;
import net.mindengine.blogix.utils.BlogixUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ClassMethodViewResolver extends ViewResolver {

    private ClassLoader[] classLoaders;
    
    public ClassMethodViewResolver(Blogix blogix, ClassLoader[] classLoaders) {
        super(blogix);
        this.classLoaders = classLoaders;
    }

    @Override
    public boolean canResolve(String view) {
        try {
            Pair<Class<?>, Method> resolver = extractClassAndMethod(view);
            if ( resolver != null ) {
                return true;
            }
        }
        catch(Exception ex) {
        }
        return false;
    }

    private Pair<Class<?>, Method> extractClassAndMethod(String view) {
        return BlogixUtils.readClassAndMethodFromParsedString(this.classLoaders, view, getDefaultPackages());
    }

    private String[] getDefaultPackages() {
        return new String[]{"views"};
    }

    @Override
    public void resolveViewAndRender(Map<String, Object> routeModel, Object objectModel, String view, OutputStream outputStream) throws Exception {
        Method method = extractClassAndMethod(view).getRight();
        
        if (!method.getReturnType().equals(String.class) && !method.getReturnType().equals(File.class)) {
            throw new IllegalArgumentException("Cannot resolve view: '" + view + "'. Reason method does not return String or File type");
        }
        
        Object result = null;
        if (method.getParameterTypes().length == 0 ) {
            result = method.invoke(null, (Object[])null);
        }
        else if(method.getParameterTypes().length == 1) {
            result = method.invoke(null, objectModel);
        }
        else throw new IllegalArgumentException("Cannot resolve view: '" + view + "'. Reason is to many method arguments for view resolver");
        
        
        
        if (result == null) {
            throw new IllegalArgumentException("Cannot resolve view: '" + view + "'. Reason method returned null");
        }
        
        if ( result instanceof String ) {
            IOUtils.write((String)result, outputStream);
        }
        else if ( result instanceof File ) {
            IOUtils.copy(new FileInputStream((File)result), outputStream);
        }
    }
    
}
