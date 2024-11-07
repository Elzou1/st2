package thkoeln.archilab.ecommerce.test.basictests;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@ArchTag("layerRules")
@AnalyzeClasses(packages = "thkoeln.archilab.ecommerce.solution")
@SuppressWarnings("PMD")
public class BasicPackageRulesTest {

    @ArchTest
    static final ArchRule noClassesOnTopLevel =
            classes().should().resideInAPackage( "..solution.*.." );

    @ArchTest
    static final ArchRule noNonIdFieldsOfTypeIUUID =
            fields()
                    .that().areDeclaredInClassesThat().areAnnotatedWith( Entity.class )
                    .and().areNotAnnotatedWith( Id.class )
                    .and().areDeclaredInClassesThat().doNotHaveSimpleName( "Product" )
                    .should().notHaveRawType( UUID.class );
}
