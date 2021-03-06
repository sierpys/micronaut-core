/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.ast.groovy

import groovy.transform.CompileStatic
import io.micronaut.ast.groovy.utils.AstMessageUtils
import io.micronaut.ast.groovy.visitor.GroovyVisitorContext
import io.micronaut.ast.groovy.visitor.LoadedVisitor
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Finishes the type element visitors.
 *
 * @author James Kleeh
 * @since 1.0
 */
@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.OUTPUT)
class TypeElementVisitorEnd implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        Map<String, LoadedVisitor> loadedVisitors = TypeElementVisitorTransform.loadedVisitors

        if (loadedVisitors != null) {
            for(loadedVisitor in loadedVisitors.values()) {
                try {
                    loadedVisitor.finish(new GroovyVisitorContext(source))
                } catch (Throwable e) {
                    AstMessageUtils.error(
                            source,
                            source.getAST(),
                            "Error finalizing type visitor [$loadedVisitor.visitor]: $e.message")
                }
            }
        }

        TypeElementVisitorTransform.loadedVisitors = null
    }
}
