/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.banditcat.android.transformer.compiler.holder;

import com.banditcat.android.transformer.compiler.helper.ModelConstants;
import com.banditcat.android.transformer.compiler.process.ProcessHolder;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import static com.sun.codemodel.JExpr.cast;
import static com.sun.codemodel.JMod.PRIVATE;

public abstract class EComponentHolder extends BaseGeneratedClassHolder {

    private static final String METHOD_MAIN_LOOPER = "getMainLooper";

	protected JExpression contextRef;
	protected JMethod init;
	private JVar resourcesRef;
	private JFieldVar powerManagerRef;
	private Map<TypeMirror, JFieldVar> databaseHelperRefs = new HashMap<TypeMirror, JFieldVar>();
	private JVar handler;

	public EComponentHolder(ProcessHolder processHolder, TypeElement annotatedElement) throws Exception {
		super(processHolder, annotatedElement);
	}





	public JVar getHandler() {
		if (handler == null) {
			setHandler();
		}
		return handler;
	}

	private void setHandler() {
		JClass handlerClass = classes().HANDLER;
        JClass looperClass = classes().LOOPER;
        JInvocation arg = JExpr._new(handlerClass).arg(looperClass.staticInvoke(METHOD_MAIN_LOOPER));
        handler = generatedClass.field(JMod.PRIVATE, handlerClass, "handler_", arg);
	}

    public JExpression getViewField(){
        return null;
    }



}
