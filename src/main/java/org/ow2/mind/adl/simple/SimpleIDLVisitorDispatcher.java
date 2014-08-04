
package org.ow2.mind.adl.simple;

import java.util.Map;
import java.util.Set;

import org.ow2.mind.AbstractVoidVisitorDispatcher;
import org.ow2.mind.VoidVisitor;
import org.ow2.mind.idl.IDLVisitor;
import org.ow2.mind.idl.ast.IDL;

import com.google.inject.Inject;

public class SimpleIDLVisitorDispatcher extends AbstractVoidVisitorDispatcher<IDL>
    implements
      IDLVisitor {

  @Inject
  @SimpleAnnotation
  protected Set<IDLVisitor> visitors;

  @Override
  protected Iterable<? extends VoidVisitor<IDL>> getVisitorsItf(
      final Map<Object, Object> context) {
    return visitors;
  }
}
