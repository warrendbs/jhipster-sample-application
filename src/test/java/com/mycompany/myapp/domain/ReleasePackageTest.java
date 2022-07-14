package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReleasePackageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleasePackage.class);
        ReleasePackage releasePackage1 = new ReleasePackage();
        releasePackage1.setId(1L);
        ReleasePackage releasePackage2 = new ReleasePackage();
        releasePackage2.setId(releasePackage1.getId());
        assertThat(releasePackage1).isEqualTo(releasePackage2);
        releasePackage2.setId(2L);
        assertThat(releasePackage1).isNotEqualTo(releasePackage2);
        releasePackage1.setId(null);
        assertThat(releasePackage1).isNotEqualTo(releasePackage2);
    }
}
