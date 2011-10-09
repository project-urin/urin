package net.sourceforge.urin;

import org.junit.Test;

import static net.sourceforge.urin.AuthorityBuilder.anAuthority;
import static net.sourceforge.urin.FragmentBuilder.aFragment;
import static net.sourceforge.urin.PathBuilder.aPath;
import static net.sourceforge.urin.QueryBuilder.aQuery;
import static net.sourceforge.urin.SchemeBuilder.aScheme;

public class UrinTest {

    @Test
    public void createsUrinWithAllParts() throws Exception {
        new Urin(aScheme(), anAuthority(), aPath(), aQuery(), aFragment());
    }

}
