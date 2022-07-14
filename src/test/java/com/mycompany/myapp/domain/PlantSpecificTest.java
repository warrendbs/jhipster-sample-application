package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlantSpecificTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlantSpecific.class);
        PlantSpecific plantSpecific1 = new PlantSpecific();
        plantSpecific1.setId(1L);
        PlantSpecific plantSpecific2 = new PlantSpecific();
        plantSpecific2.setId(plantSpecific1.getId());
        assertThat(plantSpecific1).isEqualTo(plantSpecific2);
        plantSpecific2.setId(2L);
        assertThat(plantSpecific1).isNotEqualTo(plantSpecific2);
        plantSpecific1.setId(null);
        assertThat(plantSpecific1).isNotEqualTo(plantSpecific2);
    }
}
