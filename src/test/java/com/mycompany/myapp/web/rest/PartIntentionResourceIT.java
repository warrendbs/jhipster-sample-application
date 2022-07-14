package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PartIntention;
import com.mycompany.myapp.repository.PartIntentionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PartIntentionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartIntentionResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_REVISION = "AAAAAAAAAA";
    private static final String UPDATED_REVISION = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VQI = "AAAAAAAAAA";
    private static final String UPDATED_VQI = "BBBBBBBBBB";

    private static final String DEFAULT_PROCUREMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PROCUREMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MATERIAL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MATERIAL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SERIAL_NUMBER_PROFILE = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER_PROFILE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CRITICAL_CONFIGURATION_ITEM_INDICATOR = false;
    private static final Boolean UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR = true;

    private static final String DEFAULT_REGULAR_PART_INDICATOR = "AAAAAAAAAA";
    private static final String UPDATED_REGULAR_PART_INDICATOR = "BBBBBBBBBB";

    private static final String DEFAULT_HISTORY_INDICATOR = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY_INDICATOR = "BBBBBBBBBB";

    private static final String DEFAULT_CROSS_PLANT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_CROSS_PLANT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_CROSS_PLANT_STATUS_TO_BE = "AAAAAAAAAA";
    private static final String UPDATED_CROSS_PLANT_STATUS_TO_BE = "BBBBBBBBBB";

    private static final String DEFAULT_TOOL_PACK_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_TOOL_PACK_CATEGORY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TC_CHANGE_CONTROL = false;
    private static final Boolean UPDATED_TC_CHANGE_CONTROL = true;

    private static final Boolean DEFAULT_SAP_CHANGE_CONTROL = false;
    private static final Boolean UPDATED_SAP_CHANGE_CONTROL = true;

    private static final Boolean DEFAULT_ALLOW_BOM_RESTRUCTURING = false;
    private static final Boolean UPDATED_ALLOW_BOM_RESTRUCTURING = true;

    private static final String DEFAULT_UNIT_OF_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_MEASURE = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_USAGE = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_USAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PHANTOM = false;
    private static final Boolean UPDATED_IS_PHANTOM = true;

    private static final String DEFAULT_FAILURE_RATE = "AAAAAAAAAA";
    private static final String UPDATED_FAILURE_RATE = "BBBBBBBBBB";

    private static final Long DEFAULT_IN_HOUSE_PRODUCTION_TIME = 1L;
    private static final Long UPDATED_IN_HOUSE_PRODUCTION_TIME = 2L;

    private static final String DEFAULT_SL_ABC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SL_ABC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCTION_PLANT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_PLANT = "BBBBBBBBBB";

    private static final String DEFAULT_LIMITED_DRIVING_12_NC = "AAAAAAAAAA";
    private static final String UPDATED_LIMITED_DRIVING_12_NC = "BBBBBBBBBB";

    private static final String DEFAULT_LIMITED_DRIVING_12_NCFLAG = "AAAAAAAAAA";
    private static final String UPDATED_LIMITED_DRIVING_12_NCFLAG = "BBBBBBBBBB";

    private static final String DEFAULT_MULTI_PLANT = "AAAAAAAAAA";
    private static final String UPDATED_MULTI_PLANT = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_SUCCESSOR_PART_ID = 1L;
    private static final Long UPDATED_SUCCESSOR_PART_ID = 2L;

    private static final String ENTITY_API_URL = "/api/part-intentions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartIntentionRepository partIntentionRepository;

    @Mock
    private PartIntentionRepository partIntentionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartIntentionMockMvc;

    private PartIntention partIntention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartIntention createEntity(EntityManager em) {
        PartIntention partIntention = new PartIntention()
            .productId(DEFAULT_PRODUCT_ID)
            .revision(DEFAULT_REVISION)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .vqi(DEFAULT_VQI)
            .procurementType(DEFAULT_PROCUREMENT_TYPE)
            .materialType(DEFAULT_MATERIAL_TYPE)
            .serialNumberProfile(DEFAULT_SERIAL_NUMBER_PROFILE)
            .criticalConfigurationItemIndicator(DEFAULT_CRITICAL_CONFIGURATION_ITEM_INDICATOR)
            .regularPartIndicator(DEFAULT_REGULAR_PART_INDICATOR)
            .historyIndicator(DEFAULT_HISTORY_INDICATOR)
            .crossPlantStatus(DEFAULT_CROSS_PLANT_STATUS)
            .crossPlantStatusToBe(DEFAULT_CROSS_PLANT_STATUS_TO_BE)
            .toolPackCategory(DEFAULT_TOOL_PACK_CATEGORY)
            .tcChangeControl(DEFAULT_TC_CHANGE_CONTROL)
            .sapChangeControl(DEFAULT_SAP_CHANGE_CONTROL)
            .allowBomRestructuring(DEFAULT_ALLOW_BOM_RESTRUCTURING)
            .unitOfMeasure(DEFAULT_UNIT_OF_MEASURE)
            .itemUsage(DEFAULT_ITEM_USAGE)
            .isPhantom(DEFAULT_IS_PHANTOM)
            .failureRate(DEFAULT_FAILURE_RATE)
            .inHouseProductionTime(DEFAULT_IN_HOUSE_PRODUCTION_TIME)
            .slAbcCode(DEFAULT_SL_ABC_CODE)
            .productionPlant(DEFAULT_PRODUCTION_PLANT)
            .limitedDriving12Nc(DEFAULT_LIMITED_DRIVING_12_NC)
            .limitedDriving12Ncflag(DEFAULT_LIMITED_DRIVING_12_NCFLAG)
            .multiPlant(DEFAULT_MULTI_PLANT)
            .type(DEFAULT_TYPE)
            .successorPartId(DEFAULT_SUCCESSOR_PART_ID);
        return partIntention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartIntention createUpdatedEntity(EntityManager em) {
        PartIntention partIntention = new PartIntention()
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vqi(UPDATED_VQI)
            .procurementType(UPDATED_PROCUREMENT_TYPE)
            .materialType(UPDATED_MATERIAL_TYPE)
            .serialNumberProfile(UPDATED_SERIAL_NUMBER_PROFILE)
            .criticalConfigurationItemIndicator(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR)
            .regularPartIndicator(UPDATED_REGULAR_PART_INDICATOR)
            .historyIndicator(UPDATED_HISTORY_INDICATOR)
            .crossPlantStatus(UPDATED_CROSS_PLANT_STATUS)
            .crossPlantStatusToBe(UPDATED_CROSS_PLANT_STATUS_TO_BE)
            .toolPackCategory(UPDATED_TOOL_PACK_CATEGORY)
            .tcChangeControl(UPDATED_TC_CHANGE_CONTROL)
            .sapChangeControl(UPDATED_SAP_CHANGE_CONTROL)
            .allowBomRestructuring(UPDATED_ALLOW_BOM_RESTRUCTURING)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .itemUsage(UPDATED_ITEM_USAGE)
            .isPhantom(UPDATED_IS_PHANTOM)
            .failureRate(UPDATED_FAILURE_RATE)
            .inHouseProductionTime(UPDATED_IN_HOUSE_PRODUCTION_TIME)
            .slAbcCode(UPDATED_SL_ABC_CODE)
            .productionPlant(UPDATED_PRODUCTION_PLANT)
            .limitedDriving12Nc(UPDATED_LIMITED_DRIVING_12_NC)
            .limitedDriving12Ncflag(UPDATED_LIMITED_DRIVING_12_NCFLAG)
            .multiPlant(UPDATED_MULTI_PLANT)
            .type(UPDATED_TYPE)
            .successorPartId(UPDATED_SUCCESSOR_PART_ID);
        return partIntention;
    }

    @BeforeEach
    public void initTest() {
        partIntention = createEntity(em);
    }

    @Test
    @Transactional
    void createPartIntention() throws Exception {
        int databaseSizeBeforeCreate = partIntentionRepository.findAll().size();
        // Create the PartIntention
        restPartIntentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partIntention)))
            .andExpect(status().isCreated());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeCreate + 1);
        PartIntention testPartIntention = partIntentionList.get(partIntentionList.size() - 1);
        assertThat(testPartIntention.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testPartIntention.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testPartIntention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPartIntention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPartIntention.getVqi()).isEqualTo(DEFAULT_VQI);
        assertThat(testPartIntention.getProcurementType()).isEqualTo(DEFAULT_PROCUREMENT_TYPE);
        assertThat(testPartIntention.getMaterialType()).isEqualTo(DEFAULT_MATERIAL_TYPE);
        assertThat(testPartIntention.getSerialNumberProfile()).isEqualTo(DEFAULT_SERIAL_NUMBER_PROFILE);
        assertThat(testPartIntention.getCriticalConfigurationItemIndicator()).isEqualTo(DEFAULT_CRITICAL_CONFIGURATION_ITEM_INDICATOR);
        assertThat(testPartIntention.getRegularPartIndicator()).isEqualTo(DEFAULT_REGULAR_PART_INDICATOR);
        assertThat(testPartIntention.getHistoryIndicator()).isEqualTo(DEFAULT_HISTORY_INDICATOR);
        assertThat(testPartIntention.getCrossPlantStatus()).isEqualTo(DEFAULT_CROSS_PLANT_STATUS);
        assertThat(testPartIntention.getCrossPlantStatusToBe()).isEqualTo(DEFAULT_CROSS_PLANT_STATUS_TO_BE);
        assertThat(testPartIntention.getToolPackCategory()).isEqualTo(DEFAULT_TOOL_PACK_CATEGORY);
        assertThat(testPartIntention.getTcChangeControl()).isEqualTo(DEFAULT_TC_CHANGE_CONTROL);
        assertThat(testPartIntention.getSapChangeControl()).isEqualTo(DEFAULT_SAP_CHANGE_CONTROL);
        assertThat(testPartIntention.getAllowBomRestructuring()).isEqualTo(DEFAULT_ALLOW_BOM_RESTRUCTURING);
        assertThat(testPartIntention.getUnitOfMeasure()).isEqualTo(DEFAULT_UNIT_OF_MEASURE);
        assertThat(testPartIntention.getItemUsage()).isEqualTo(DEFAULT_ITEM_USAGE);
        assertThat(testPartIntention.getIsPhantom()).isEqualTo(DEFAULT_IS_PHANTOM);
        assertThat(testPartIntention.getFailureRate()).isEqualTo(DEFAULT_FAILURE_RATE);
        assertThat(testPartIntention.getInHouseProductionTime()).isEqualTo(DEFAULT_IN_HOUSE_PRODUCTION_TIME);
        assertThat(testPartIntention.getSlAbcCode()).isEqualTo(DEFAULT_SL_ABC_CODE);
        assertThat(testPartIntention.getProductionPlant()).isEqualTo(DEFAULT_PRODUCTION_PLANT);
        assertThat(testPartIntention.getLimitedDriving12Nc()).isEqualTo(DEFAULT_LIMITED_DRIVING_12_NC);
        assertThat(testPartIntention.getLimitedDriving12Ncflag()).isEqualTo(DEFAULT_LIMITED_DRIVING_12_NCFLAG);
        assertThat(testPartIntention.getMultiPlant()).isEqualTo(DEFAULT_MULTI_PLANT);
        assertThat(testPartIntention.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPartIntention.getSuccessorPartId()).isEqualTo(DEFAULT_SUCCESSOR_PART_ID);
    }

    @Test
    @Transactional
    void createPartIntentionWithExistingId() throws Exception {
        // Create the PartIntention with an existing ID
        partIntention.setId(1L);

        int databaseSizeBeforeCreate = partIntentionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartIntentionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partIntention)))
            .andExpect(status().isBadRequest());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartIntentions() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        // Get all the partIntentionList
        restPartIntentionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partIntention.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].revision").value(hasItem(DEFAULT_REVISION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vqi").value(hasItem(DEFAULT_VQI)))
            .andExpect(jsonPath("$.[*].procurementType").value(hasItem(DEFAULT_PROCUREMENT_TYPE)))
            .andExpect(jsonPath("$.[*].materialType").value(hasItem(DEFAULT_MATERIAL_TYPE)))
            .andExpect(jsonPath("$.[*].serialNumberProfile").value(hasItem(DEFAULT_SERIAL_NUMBER_PROFILE)))
            .andExpect(
                jsonPath("$.[*].criticalConfigurationItemIndicator")
                    .value(hasItem(DEFAULT_CRITICAL_CONFIGURATION_ITEM_INDICATOR.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].regularPartIndicator").value(hasItem(DEFAULT_REGULAR_PART_INDICATOR)))
            .andExpect(jsonPath("$.[*].historyIndicator").value(hasItem(DEFAULT_HISTORY_INDICATOR)))
            .andExpect(jsonPath("$.[*].crossPlantStatus").value(hasItem(DEFAULT_CROSS_PLANT_STATUS)))
            .andExpect(jsonPath("$.[*].crossPlantStatusToBe").value(hasItem(DEFAULT_CROSS_PLANT_STATUS_TO_BE)))
            .andExpect(jsonPath("$.[*].toolPackCategory").value(hasItem(DEFAULT_TOOL_PACK_CATEGORY)))
            .andExpect(jsonPath("$.[*].tcChangeControl").value(hasItem(DEFAULT_TC_CHANGE_CONTROL.booleanValue())))
            .andExpect(jsonPath("$.[*].sapChangeControl").value(hasItem(DEFAULT_SAP_CHANGE_CONTROL.booleanValue())))
            .andExpect(jsonPath("$.[*].allowBomRestructuring").value(hasItem(DEFAULT_ALLOW_BOM_RESTRUCTURING.booleanValue())))
            .andExpect(jsonPath("$.[*].unitOfMeasure").value(hasItem(DEFAULT_UNIT_OF_MEASURE)))
            .andExpect(jsonPath("$.[*].itemUsage").value(hasItem(DEFAULT_ITEM_USAGE)))
            .andExpect(jsonPath("$.[*].isPhantom").value(hasItem(DEFAULT_IS_PHANTOM.booleanValue())))
            .andExpect(jsonPath("$.[*].failureRate").value(hasItem(DEFAULT_FAILURE_RATE)))
            .andExpect(jsonPath("$.[*].inHouseProductionTime").value(hasItem(DEFAULT_IN_HOUSE_PRODUCTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].slAbcCode").value(hasItem(DEFAULT_SL_ABC_CODE)))
            .andExpect(jsonPath("$.[*].productionPlant").value(hasItem(DEFAULT_PRODUCTION_PLANT)))
            .andExpect(jsonPath("$.[*].limitedDriving12Nc").value(hasItem(DEFAULT_LIMITED_DRIVING_12_NC)))
            .andExpect(jsonPath("$.[*].limitedDriving12Ncflag").value(hasItem(DEFAULT_LIMITED_DRIVING_12_NCFLAG)))
            .andExpect(jsonPath("$.[*].multiPlant").value(hasItem(DEFAULT_MULTI_PLANT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].successorPartId").value(hasItem(DEFAULT_SUCCESSOR_PART_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartIntentionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(partIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartIntentionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partIntentionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartIntentionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partIntentionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPartIntention() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        // Get the partIntention
        restPartIntentionMockMvc
            .perform(get(ENTITY_API_URL_ID, partIntention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partIntention.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.revision").value(DEFAULT_REVISION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.vqi").value(DEFAULT_VQI))
            .andExpect(jsonPath("$.procurementType").value(DEFAULT_PROCUREMENT_TYPE))
            .andExpect(jsonPath("$.materialType").value(DEFAULT_MATERIAL_TYPE))
            .andExpect(jsonPath("$.serialNumberProfile").value(DEFAULT_SERIAL_NUMBER_PROFILE))
            .andExpect(jsonPath("$.criticalConfigurationItemIndicator").value(DEFAULT_CRITICAL_CONFIGURATION_ITEM_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.regularPartIndicator").value(DEFAULT_REGULAR_PART_INDICATOR))
            .andExpect(jsonPath("$.historyIndicator").value(DEFAULT_HISTORY_INDICATOR))
            .andExpect(jsonPath("$.crossPlantStatus").value(DEFAULT_CROSS_PLANT_STATUS))
            .andExpect(jsonPath("$.crossPlantStatusToBe").value(DEFAULT_CROSS_PLANT_STATUS_TO_BE))
            .andExpect(jsonPath("$.toolPackCategory").value(DEFAULT_TOOL_PACK_CATEGORY))
            .andExpect(jsonPath("$.tcChangeControl").value(DEFAULT_TC_CHANGE_CONTROL.booleanValue()))
            .andExpect(jsonPath("$.sapChangeControl").value(DEFAULT_SAP_CHANGE_CONTROL.booleanValue()))
            .andExpect(jsonPath("$.allowBomRestructuring").value(DEFAULT_ALLOW_BOM_RESTRUCTURING.booleanValue()))
            .andExpect(jsonPath("$.unitOfMeasure").value(DEFAULT_UNIT_OF_MEASURE))
            .andExpect(jsonPath("$.itemUsage").value(DEFAULT_ITEM_USAGE))
            .andExpect(jsonPath("$.isPhantom").value(DEFAULT_IS_PHANTOM.booleanValue()))
            .andExpect(jsonPath("$.failureRate").value(DEFAULT_FAILURE_RATE))
            .andExpect(jsonPath("$.inHouseProductionTime").value(DEFAULT_IN_HOUSE_PRODUCTION_TIME.intValue()))
            .andExpect(jsonPath("$.slAbcCode").value(DEFAULT_SL_ABC_CODE))
            .andExpect(jsonPath("$.productionPlant").value(DEFAULT_PRODUCTION_PLANT))
            .andExpect(jsonPath("$.limitedDriving12Nc").value(DEFAULT_LIMITED_DRIVING_12_NC))
            .andExpect(jsonPath("$.limitedDriving12Ncflag").value(DEFAULT_LIMITED_DRIVING_12_NCFLAG))
            .andExpect(jsonPath("$.multiPlant").value(DEFAULT_MULTI_PLANT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.successorPartId").value(DEFAULT_SUCCESSOR_PART_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPartIntention() throws Exception {
        // Get the partIntention
        restPartIntentionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartIntention() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();

        // Update the partIntention
        PartIntention updatedPartIntention = partIntentionRepository.findById(partIntention.getId()).get();
        // Disconnect from session so that the updates on updatedPartIntention are not directly saved in db
        em.detach(updatedPartIntention);
        updatedPartIntention
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vqi(UPDATED_VQI)
            .procurementType(UPDATED_PROCUREMENT_TYPE)
            .materialType(UPDATED_MATERIAL_TYPE)
            .serialNumberProfile(UPDATED_SERIAL_NUMBER_PROFILE)
            .criticalConfigurationItemIndicator(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR)
            .regularPartIndicator(UPDATED_REGULAR_PART_INDICATOR)
            .historyIndicator(UPDATED_HISTORY_INDICATOR)
            .crossPlantStatus(UPDATED_CROSS_PLANT_STATUS)
            .crossPlantStatusToBe(UPDATED_CROSS_PLANT_STATUS_TO_BE)
            .toolPackCategory(UPDATED_TOOL_PACK_CATEGORY)
            .tcChangeControl(UPDATED_TC_CHANGE_CONTROL)
            .sapChangeControl(UPDATED_SAP_CHANGE_CONTROL)
            .allowBomRestructuring(UPDATED_ALLOW_BOM_RESTRUCTURING)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .itemUsage(UPDATED_ITEM_USAGE)
            .isPhantom(UPDATED_IS_PHANTOM)
            .failureRate(UPDATED_FAILURE_RATE)
            .inHouseProductionTime(UPDATED_IN_HOUSE_PRODUCTION_TIME)
            .slAbcCode(UPDATED_SL_ABC_CODE)
            .productionPlant(UPDATED_PRODUCTION_PLANT)
            .limitedDriving12Nc(UPDATED_LIMITED_DRIVING_12_NC)
            .limitedDriving12Ncflag(UPDATED_LIMITED_DRIVING_12_NCFLAG)
            .multiPlant(UPDATED_MULTI_PLANT)
            .type(UPDATED_TYPE)
            .successorPartId(UPDATED_SUCCESSOR_PART_ID);

        restPartIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartIntention))
            )
            .andExpect(status().isOk());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
        PartIntention testPartIntention = partIntentionList.get(partIntentionList.size() - 1);
        assertThat(testPartIntention.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testPartIntention.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testPartIntention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPartIntention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPartIntention.getVqi()).isEqualTo(UPDATED_VQI);
        assertThat(testPartIntention.getProcurementType()).isEqualTo(UPDATED_PROCUREMENT_TYPE);
        assertThat(testPartIntention.getMaterialType()).isEqualTo(UPDATED_MATERIAL_TYPE);
        assertThat(testPartIntention.getSerialNumberProfile()).isEqualTo(UPDATED_SERIAL_NUMBER_PROFILE);
        assertThat(testPartIntention.getCriticalConfigurationItemIndicator()).isEqualTo(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR);
        assertThat(testPartIntention.getRegularPartIndicator()).isEqualTo(UPDATED_REGULAR_PART_INDICATOR);
        assertThat(testPartIntention.getHistoryIndicator()).isEqualTo(UPDATED_HISTORY_INDICATOR);
        assertThat(testPartIntention.getCrossPlantStatus()).isEqualTo(UPDATED_CROSS_PLANT_STATUS);
        assertThat(testPartIntention.getCrossPlantStatusToBe()).isEqualTo(UPDATED_CROSS_PLANT_STATUS_TO_BE);
        assertThat(testPartIntention.getToolPackCategory()).isEqualTo(UPDATED_TOOL_PACK_CATEGORY);
        assertThat(testPartIntention.getTcChangeControl()).isEqualTo(UPDATED_TC_CHANGE_CONTROL);
        assertThat(testPartIntention.getSapChangeControl()).isEqualTo(UPDATED_SAP_CHANGE_CONTROL);
        assertThat(testPartIntention.getAllowBomRestructuring()).isEqualTo(UPDATED_ALLOW_BOM_RESTRUCTURING);
        assertThat(testPartIntention.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testPartIntention.getItemUsage()).isEqualTo(UPDATED_ITEM_USAGE);
        assertThat(testPartIntention.getIsPhantom()).isEqualTo(UPDATED_IS_PHANTOM);
        assertThat(testPartIntention.getFailureRate()).isEqualTo(UPDATED_FAILURE_RATE);
        assertThat(testPartIntention.getInHouseProductionTime()).isEqualTo(UPDATED_IN_HOUSE_PRODUCTION_TIME);
        assertThat(testPartIntention.getSlAbcCode()).isEqualTo(UPDATED_SL_ABC_CODE);
        assertThat(testPartIntention.getProductionPlant()).isEqualTo(UPDATED_PRODUCTION_PLANT);
        assertThat(testPartIntention.getLimitedDriving12Nc()).isEqualTo(UPDATED_LIMITED_DRIVING_12_NC);
        assertThat(testPartIntention.getLimitedDriving12Ncflag()).isEqualTo(UPDATED_LIMITED_DRIVING_12_NCFLAG);
        assertThat(testPartIntention.getMultiPlant()).isEqualTo(UPDATED_MULTI_PLANT);
        assertThat(testPartIntention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPartIntention.getSuccessorPartId()).isEqualTo(UPDATED_SUCCESSOR_PART_ID);
    }

    @Test
    @Transactional
    void putNonExistingPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partIntention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partIntention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartIntentionWithPatch() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();

        // Update the partIntention using partial update
        PartIntention partialUpdatedPartIntention = new PartIntention();
        partialUpdatedPartIntention.setId(partIntention.getId());

        partialUpdatedPartIntention
            .productId(UPDATED_PRODUCT_ID)
            .procurementType(UPDATED_PROCUREMENT_TYPE)
            .criticalConfigurationItemIndicator(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR)
            .regularPartIndicator(UPDATED_REGULAR_PART_INDICATOR)
            .crossPlantStatusToBe(UPDATED_CROSS_PLANT_STATUS_TO_BE)
            .tcChangeControl(UPDATED_TC_CHANGE_CONTROL)
            .allowBomRestructuring(UPDATED_ALLOW_BOM_RESTRUCTURING)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .itemUsage(UPDATED_ITEM_USAGE)
            .isPhantom(UPDATED_IS_PHANTOM)
            .inHouseProductionTime(UPDATED_IN_HOUSE_PRODUCTION_TIME)
            .slAbcCode(UPDATED_SL_ABC_CODE)
            .multiPlant(UPDATED_MULTI_PLANT)
            .type(UPDATED_TYPE);

        restPartIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartIntention))
            )
            .andExpect(status().isOk());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
        PartIntention testPartIntention = partIntentionList.get(partIntentionList.size() - 1);
        assertThat(testPartIntention.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testPartIntention.getRevision()).isEqualTo(DEFAULT_REVISION);
        assertThat(testPartIntention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPartIntention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPartIntention.getVqi()).isEqualTo(DEFAULT_VQI);
        assertThat(testPartIntention.getProcurementType()).isEqualTo(UPDATED_PROCUREMENT_TYPE);
        assertThat(testPartIntention.getMaterialType()).isEqualTo(DEFAULT_MATERIAL_TYPE);
        assertThat(testPartIntention.getSerialNumberProfile()).isEqualTo(DEFAULT_SERIAL_NUMBER_PROFILE);
        assertThat(testPartIntention.getCriticalConfigurationItemIndicator()).isEqualTo(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR);
        assertThat(testPartIntention.getRegularPartIndicator()).isEqualTo(UPDATED_REGULAR_PART_INDICATOR);
        assertThat(testPartIntention.getHistoryIndicator()).isEqualTo(DEFAULT_HISTORY_INDICATOR);
        assertThat(testPartIntention.getCrossPlantStatus()).isEqualTo(DEFAULT_CROSS_PLANT_STATUS);
        assertThat(testPartIntention.getCrossPlantStatusToBe()).isEqualTo(UPDATED_CROSS_PLANT_STATUS_TO_BE);
        assertThat(testPartIntention.getToolPackCategory()).isEqualTo(DEFAULT_TOOL_PACK_CATEGORY);
        assertThat(testPartIntention.getTcChangeControl()).isEqualTo(UPDATED_TC_CHANGE_CONTROL);
        assertThat(testPartIntention.getSapChangeControl()).isEqualTo(DEFAULT_SAP_CHANGE_CONTROL);
        assertThat(testPartIntention.getAllowBomRestructuring()).isEqualTo(UPDATED_ALLOW_BOM_RESTRUCTURING);
        assertThat(testPartIntention.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testPartIntention.getItemUsage()).isEqualTo(UPDATED_ITEM_USAGE);
        assertThat(testPartIntention.getIsPhantom()).isEqualTo(UPDATED_IS_PHANTOM);
        assertThat(testPartIntention.getFailureRate()).isEqualTo(DEFAULT_FAILURE_RATE);
        assertThat(testPartIntention.getInHouseProductionTime()).isEqualTo(UPDATED_IN_HOUSE_PRODUCTION_TIME);
        assertThat(testPartIntention.getSlAbcCode()).isEqualTo(UPDATED_SL_ABC_CODE);
        assertThat(testPartIntention.getProductionPlant()).isEqualTo(DEFAULT_PRODUCTION_PLANT);
        assertThat(testPartIntention.getLimitedDriving12Nc()).isEqualTo(DEFAULT_LIMITED_DRIVING_12_NC);
        assertThat(testPartIntention.getLimitedDriving12Ncflag()).isEqualTo(DEFAULT_LIMITED_DRIVING_12_NCFLAG);
        assertThat(testPartIntention.getMultiPlant()).isEqualTo(UPDATED_MULTI_PLANT);
        assertThat(testPartIntention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPartIntention.getSuccessorPartId()).isEqualTo(DEFAULT_SUCCESSOR_PART_ID);
    }

    @Test
    @Transactional
    void fullUpdatePartIntentionWithPatch() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();

        // Update the partIntention using partial update
        PartIntention partialUpdatedPartIntention = new PartIntention();
        partialUpdatedPartIntention.setId(partIntention.getId());

        partialUpdatedPartIntention
            .productId(UPDATED_PRODUCT_ID)
            .revision(UPDATED_REVISION)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .vqi(UPDATED_VQI)
            .procurementType(UPDATED_PROCUREMENT_TYPE)
            .materialType(UPDATED_MATERIAL_TYPE)
            .serialNumberProfile(UPDATED_SERIAL_NUMBER_PROFILE)
            .criticalConfigurationItemIndicator(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR)
            .regularPartIndicator(UPDATED_REGULAR_PART_INDICATOR)
            .historyIndicator(UPDATED_HISTORY_INDICATOR)
            .crossPlantStatus(UPDATED_CROSS_PLANT_STATUS)
            .crossPlantStatusToBe(UPDATED_CROSS_PLANT_STATUS_TO_BE)
            .toolPackCategory(UPDATED_TOOL_PACK_CATEGORY)
            .tcChangeControl(UPDATED_TC_CHANGE_CONTROL)
            .sapChangeControl(UPDATED_SAP_CHANGE_CONTROL)
            .allowBomRestructuring(UPDATED_ALLOW_BOM_RESTRUCTURING)
            .unitOfMeasure(UPDATED_UNIT_OF_MEASURE)
            .itemUsage(UPDATED_ITEM_USAGE)
            .isPhantom(UPDATED_IS_PHANTOM)
            .failureRate(UPDATED_FAILURE_RATE)
            .inHouseProductionTime(UPDATED_IN_HOUSE_PRODUCTION_TIME)
            .slAbcCode(UPDATED_SL_ABC_CODE)
            .productionPlant(UPDATED_PRODUCTION_PLANT)
            .limitedDriving12Nc(UPDATED_LIMITED_DRIVING_12_NC)
            .limitedDriving12Ncflag(UPDATED_LIMITED_DRIVING_12_NCFLAG)
            .multiPlant(UPDATED_MULTI_PLANT)
            .type(UPDATED_TYPE)
            .successorPartId(UPDATED_SUCCESSOR_PART_ID);

        restPartIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartIntention))
            )
            .andExpect(status().isOk());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
        PartIntention testPartIntention = partIntentionList.get(partIntentionList.size() - 1);
        assertThat(testPartIntention.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testPartIntention.getRevision()).isEqualTo(UPDATED_REVISION);
        assertThat(testPartIntention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPartIntention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPartIntention.getVqi()).isEqualTo(UPDATED_VQI);
        assertThat(testPartIntention.getProcurementType()).isEqualTo(UPDATED_PROCUREMENT_TYPE);
        assertThat(testPartIntention.getMaterialType()).isEqualTo(UPDATED_MATERIAL_TYPE);
        assertThat(testPartIntention.getSerialNumberProfile()).isEqualTo(UPDATED_SERIAL_NUMBER_PROFILE);
        assertThat(testPartIntention.getCriticalConfigurationItemIndicator()).isEqualTo(UPDATED_CRITICAL_CONFIGURATION_ITEM_INDICATOR);
        assertThat(testPartIntention.getRegularPartIndicator()).isEqualTo(UPDATED_REGULAR_PART_INDICATOR);
        assertThat(testPartIntention.getHistoryIndicator()).isEqualTo(UPDATED_HISTORY_INDICATOR);
        assertThat(testPartIntention.getCrossPlantStatus()).isEqualTo(UPDATED_CROSS_PLANT_STATUS);
        assertThat(testPartIntention.getCrossPlantStatusToBe()).isEqualTo(UPDATED_CROSS_PLANT_STATUS_TO_BE);
        assertThat(testPartIntention.getToolPackCategory()).isEqualTo(UPDATED_TOOL_PACK_CATEGORY);
        assertThat(testPartIntention.getTcChangeControl()).isEqualTo(UPDATED_TC_CHANGE_CONTROL);
        assertThat(testPartIntention.getSapChangeControl()).isEqualTo(UPDATED_SAP_CHANGE_CONTROL);
        assertThat(testPartIntention.getAllowBomRestructuring()).isEqualTo(UPDATED_ALLOW_BOM_RESTRUCTURING);
        assertThat(testPartIntention.getUnitOfMeasure()).isEqualTo(UPDATED_UNIT_OF_MEASURE);
        assertThat(testPartIntention.getItemUsage()).isEqualTo(UPDATED_ITEM_USAGE);
        assertThat(testPartIntention.getIsPhantom()).isEqualTo(UPDATED_IS_PHANTOM);
        assertThat(testPartIntention.getFailureRate()).isEqualTo(UPDATED_FAILURE_RATE);
        assertThat(testPartIntention.getInHouseProductionTime()).isEqualTo(UPDATED_IN_HOUSE_PRODUCTION_TIME);
        assertThat(testPartIntention.getSlAbcCode()).isEqualTo(UPDATED_SL_ABC_CODE);
        assertThat(testPartIntention.getProductionPlant()).isEqualTo(UPDATED_PRODUCTION_PLANT);
        assertThat(testPartIntention.getLimitedDriving12Nc()).isEqualTo(UPDATED_LIMITED_DRIVING_12_NC);
        assertThat(testPartIntention.getLimitedDriving12Ncflag()).isEqualTo(UPDATED_LIMITED_DRIVING_12_NCFLAG);
        assertThat(testPartIntention.getMultiPlant()).isEqualTo(UPDATED_MULTI_PLANT);
        assertThat(testPartIntention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPartIntention.getSuccessorPartId()).isEqualTo(UPDATED_SUCCESSOR_PART_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partIntention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partIntention))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartIntention() throws Exception {
        int databaseSizeBeforeUpdate = partIntentionRepository.findAll().size();
        partIntention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartIntentionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partIntention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartIntention in the database
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartIntention() throws Exception {
        // Initialize the database
        partIntentionRepository.saveAndFlush(partIntention);

        int databaseSizeBeforeDelete = partIntentionRepository.findAll().size();

        // Delete the partIntention
        restPartIntentionMockMvc
            .perform(delete(ENTITY_API_URL_ID, partIntention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartIntention> partIntentionList = partIntentionRepository.findAll();
        assertThat(partIntentionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
