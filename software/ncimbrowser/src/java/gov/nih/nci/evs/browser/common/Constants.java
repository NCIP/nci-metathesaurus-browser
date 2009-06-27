package gov.nih.nci.evs.browser.common;

import java.util.Arrays;
import java.util.List;

/**
 * Application constants class
 * @author garciawa2
 */
public class Constants {

    // Application version
    static public final int MAJOR_VER = 1;
    static public final int MINOR_VER = 0;
    static public final String CONFIG_FILE = "NCImBrowserProperties.xml";
    static public final String CODING_SCHEME_NAME = "NCI MetaThesaurus";

	// Application constants
	static public final String NA = "N/A";
	static public final String TRUE = "true";
	static public final String FALSE = "false";
	static public final String EMPTY = "";

    static public final List<String> REL = Arrays.asList(new String[] {
		"Parent	PAR	Concept subsumes current concept as a subtype, part, or otherwise.",
		"Child	CHD	Concept is subsumed by current concept as its subtype, part, or otherwise.",
		"Broader	RB	Concept is broader than current concept in some less specific way.",
		"Narrower	RN	Concept is narrower than current concept in some less specific way.",
		"Sibling	SIB	Concept is subsumed by a parent shared with the current concept.",
		"Other	AQ	[None]",
		"Other	QB	[None]",
		"Other	RO	Concept is related in some other way.",
		"Other	RQ	[None]",
		"Other	SY	[None]"
        });

    static public final List<String> RELA = Arrays.asList(new String[] {
			"Parent	PAR	[none]",
			"Parent	PAR	contains",
			"Parent	PAR	has_branch",
			"Parent	PAR	has_part",
			"Parent	PAR	has_regional_part",
			"Parent	PAR	has_segment",
			"Parent	PAR	has_tributary",
			"Parent	PAR	inverse_isa",
			"Child	CHD	[none]",
			"Child	CHD	branch_of",
			"Child	CHD	contained_in",
			"Child	CHD	isa",
			"Child	CHD	part_of",
			"Child	CHD	regional_part_of",
			"Child	CHD	segment_of",
			"Child	CHD	tributary_of",
			"Broader	RB	[none]",
			"Broader	RB	Has_Conceptual_Part",
			"Broader	RB	has_conceptual_part",
			"Broader	RB	has_form",
			"Broader	RB	has_part",
			"Broader	RB	has_tradename",
			"Broader	RB	has_version",
			"Broader	RB	inverse_isa",
			"Broader	RB	mapped_from",
			"Broader	RB	precise_ingredient_of",
			"Narrower	RN	[none]",
			"Narrower	RN	Conceptual_Part_Of",
			"Narrower	RN	conceptual_part_of",
			"Narrower	RN	form_of",
			"Narrower	RN	has_precise_ingredient",
			"Narrower	RN	isa",
			"Narrower	RN	mapped_to",
			"Narrower	RN	part_of",
			"Narrower	RN	tradename_of",
			"Narrower	RN	version_of",
			"Sibling	SIB	[none]",
			"Sibling	SIB	sib_in_branch_of",
			"Sibling	SIB	sib_in_isa",
			"Sibling	SIB	sib_in_part_of",
			"Sibling	SIB	sib_in_tributary_of",
			"Other	AQ	[none]",
			"Other	QB	[none]",
			"Other	RO	[none]",
			"Other	RO	3_UTR_of",
			"Other	RO	5_UTR_of",
			"Other	RO	Abnormal_Cell_Affected_By_Chemical_Or_Drug",
			"Other	RO	Abnormality_Associated_With_Allele",
			"Other	RO	access_device_used_by",
			"Other	RO	access_of",
			"Other	RO	action_of",
			"Other	RO	active_ingredient_of",
			"Other	RO	Activity_Of_Allele",
			"Other	RO	Allele_Absent_From_Wild-type_Chromosomal_Location",
			"Other	RO	Allele_Associated_With_Disease",
			"Other	RO	Allele_Has_Abnormality",
			"Other	RO	Allele_Has_Activity",
			"Other	RO	Allele_In_Chromosomal_Location",
			"Other	RO	Allele_Not_Associated_With_Disease",
			"Other	RO	Allele_of",
			"Other	RO	Allele_Plays_Altered_Role_In_Process",
			"Other	RO	Allele_Plays_Role_In_Metabolism_Of_Chemical_Or_Drug",
			"Other	RO	allelic_variant_of",
			"Other	RO	Amino_Acid_Variant_of",
			"Other	RO	analyzed_by",
			"Other	RO	analyzes",
			"Other	RO	Anatomic_Structure_Has_Location",
			"Other	RO	Anatomic_Structure_Is_Physical_Part_Of",
			"Other	RO	Anatomy_Originated_From_Biological_Process",
			"Other	RO	Aneuploidy_Addition_of",
			"Other	RO	Aneuploidy_Deletion_of",
			"Other	RO	approach_of",
			"Other	RO	Arm_Location_of",
			"Other	RO	Arm_of",
			"Other	RO	associated_disease",
			"Other	RO	associated_finding_of",
			"Other	RO	associated_genetic_condition",
			"Other	RO	associated_morphology_of",
			"Other	RO	associated_procedure_of",
			"Other	RO	associated_with",
			"Other	RO	Associated_With_Malfunction_Of_Gene_Product",
			"Other	RO	Band_Location_of",
			"Other	RO	Band_of",
			"Other	RO	Biological_Process_Has_Associated_Location",
			"Other	RO	Biological_Process_Has_Initiator_Chemical_Or_Drug",
			"Other	RO	Biological_Process_Has_Initiator_Process",
			"Other	RO	Biological_Process_Has_Result_Anatomy",
			"Other	RO	Biological_Process_Has_Result_Biological_Process",
			"Other	RO	Biological_Process_Has_Result_Chemical_Or_Drug",
			"Other	RO	Biological_Process_Involves_Chemical_Or_Drug",
			"Other	RO	Biological_Process_Involves_Gene_Product",
			"Other	RO	Biological_Process_Is_Part_Of_Process",
			"Other	RO	Biological_Process_Results_From_Biological_Process",
			"Other	RO	Biomarker_Type_Includes_Gene",
			"Other	RO	Biomarker_Type_Includes_Gene_Product",
			"Other	RO	branch_of",
			"Other	RO	causative_agent_of",
			"Other	RO	cause_of",
			"Other	RO	Cell_Type_Is_Associated_With_EO_Disease",
			"Other	RO	Cell_Type_Or_Tissue_Affected_By_Chemical_Or_Drug",
			"Other	RO	Centromere_of",
			"Other	RO	CH3_Status_of",
			"Other	RO	Chemical_Or_Drug_Affects_Abnormal_Cell",
			"Other	RO	Chemical_Or_Drug_Affects_Cell_Type_Or_Tissue",
			"Other	RO	Chemical_Or_Drug_Affects_Gene_Product",
			"Other	RO	Chemical_Or_Drug_Has_Mechanism_Of_Action",
			"Other	RO	Chemical_Or_Drug_Has_Physiologic_Effect",
			"Other	RO	Chemical_Or_Drug_Initiates_Biological_Process",
			"Other	RO	Chemical_Or_Drug_Is_Metabolized_By_Enzyme",
			"Other	RO	Chemical_Or_Drug_Is_Product_Of_Biological_Process",
			"Other	RO	Chemical_Or_Drug_Metabolism_Is_Associated_With_Allele",
			"Other	RO	Chemical_Or_Drug_Plays_Role_In_Biological_Process",
			"Other	RO	Chemotherapy_Regimen_Has_Component",
			"Other	RO	Chromosomal_Location_of",
			"Other	RO	Chromosomal_Location_Of_Allele",
			"Other	RO	Chromosomal_Location_of_Wild-type_Gene",
			"Other	RO	Chromosomal_Structural_Variant",
			"Other	RO	clinical_course_of",
			"Other	RO	Completely_Excised_Anatomy_Has_Procedure",
			"Other	RO	Completely_Excised_Anatomy_May_Have_Procedure",
			"Other	RO	Complex_Has_Physical_Part",
			"Other	RO	component_of",
			"Other	RO	Concept_In_Subset",
			"Other	RO	consists_of",
			"Other	RO	Constituent_Amino_Acid_of",
			"Other	RO	Constituent_Element_of",
			"Other	RO	Constituent_Protein_of",
			"Other	RO	Constituent_Variant_of",
			"Other	RO	constitutes",
			"Other	RO	continuous_with",
			"Other	RO	contraindicated_with",
			"Other	RO	definitional_manifestation_of",
			"Other	RO	degree_of",
			"Other	RO	Deleted_Region_End_Band",
			"Other	RO	Deleted_Region_Start_Band",
			"Other	RO	device_used_by",
			"Other	RO	diagnosed_by",
			"Other	RO	diagnoses",
			"Other	RO	direct_device_of",
			"Other	RO	direct_morphology_of",
			"Other	RO	direct_procedure_site_of",
			"Other	RO	direct_substance_of",
			"Other	RO	Disease_Associated_With_Allele",
			"Other	RO	Disease_Excludes_Abnormal_Cell",
			"Other	RO	Disease_Excludes_Cytogenetic_Abnormality",
			"Other	RO	Disease_Excludes_Finding",
			"Other	RO	Disease_Excludes_Molecular_Abnormality",
			"Other	RO	Disease_Excludes_Normal_Cell_Origin",
			"Other	RO	Disease_Excludes_Normal_Tissue_Origin",
			"Other	RO	Disease_Excludes_Primary_Anatomic_Site",
			"Other	RO	Disease_Has_Abnormal_Cell",
			"Other	RO	Disease_Has_Accepted_Treatment_With_Regimen",
			"Other	RO	Disease_Has_Associated_Anatomic_Site",
			"Other	RO	Disease_Has_Associated_Disease",
			"Other	RO	Disease_Has_Associated_Gene",
			"Other	RO	Disease_Has_Cytogenetic_Abnormality",
			"Other	RO	Disease_Has_Finding",
			"Other	RO	Disease_Has_Metastatic_Anatomic_Site",
			"Other	RO	Disease_Has_Molecular_Abnormality",
			"Other	RO	Disease_Has_Normal_Cell_Origin",
			"Other	RO	Disease_Has_Normal_Tissue_Origin",
			"Other	RO	Disease_Has_Primary_Anatomic_Site",
			"Other	RO	Disease_Is_Grade",
			"Other	RO	Disease_Is_Marked_By_Gene",
			"Other	RO	Disease_Is_Stage",
			"Other	RO	Disease_May_Have_Abnormal_Cell",
			"Other	RO	Disease_May_Have_Associated_Disease",
			"Other	RO	Disease_May_Have_Cytogenetic_Abnormality",
			"Other	RO	Disease_May_Have_Finding",
			"Other	RO	Disease_May_Have_Molecular_Abnormality",
			"Other	RO	Disease_May_Have_Normal_Cell_Origin",
			"Other	RO	Disease_Not_Associated_With_Allele",
			"Other	RO	domain_of",
			"Other	RO	dose_form_of",
			"Other	RO	drug_contraindicated_for",
			"Other	RO	due_to",
			"Other	RO	Duplicated_Region_End_Band",
			"Other	RO	Duplicated_Region_Start_Band",
			"Other	RO	Effect_of",
			"Other	RO	Encoded_by",
			"Other	RO	Encodes",
			"Other	RO	Endogenous_Product_Related_To",
			"Other	RO	energy_used_by",
			"Other	RO	entry_combination_of",
			"Other	RO	Enzyme_Metabolizes_Chemical_Or_Drug",
			"Other	RO	EO_Anatomy_Is_Associated_With_EO_Disease",
			"Other	RO	EO_Disease_Has_Associated_Cell_Type",
			"Other	RO	EO_Disease_Has_Associated_EO_Anatomy",
			"Other	RO	EO_Disease_Has_Property_Or_Attribute",
			"Other	RO	EO_Disease_Maps_To_Human_Disease",
			"Other	RO	episodicity_of",
			"Other	RO	evaluation_of",
			"Other	RO	Excised_Anatomy_Has_Procedure",
			"Other	RO	Excised_Anatomy_May_Have_Procedure",
			"Other	RO	exhibited_by",
			"Other	RO	exhibits",
			"Other	RO	Exon_of",
			"Other	RO	Feature_of",
			"Other	RO	finding_context_of",
			"Other	RO	finding_informer_of",
			"Other	RO	finding_method_of",
			"Other	RO	finding_site_of",
			"Other	RO	focus_of",
			"Other	RO	Gene_Associated_With_Disease",
			"Other	RO	Gene_Encodes_Gene_Product",
			"Other	RO	Gene_Found_In_Organism",
			"Other	RO	Gene_Has_Abnormality",
			"Other	RO	Gene_Has_Physical_Location",
			"Other	RO	Gene_In_Chromosomal_Location",
			"Other	RO	Gene_Is_Biomarker_Of",
			"Other	RO	Gene_Is_Biomarker_Type",
			"Other	RO	Gene_Is_Element_In_Pathway",
			"Other	RO	Gene_Location_of",
			"Other	RO	Gene_of",
			"Other	RO	Gene_Plays_Role_In_Process",
			"Other	RO	Gene_Product_Affected_By_Chemical_Or_Drug",
			"Other	RO	Gene_Product_Encoded_By_Gene",
			"Other	RO	Gene_Product_Expressed_In_Tissue",
			"Other	RO	Gene_Product_Has_Abnormality",
			"Other	RO	Gene_Product_Has_Associated_Anatomy",
			"Other	RO	Gene_Product_Has_Biochemical_Function",
			"Other	RO	Gene_Product_Has_Chemical_Classification",
			"Other	RO	Gene_Product_Has_Malfunction_Type",
			"Other	RO	Gene_Product_Has_Organism_Source",
			"Other	RO	Gene_Product_Has_Structural_Domain_Or_Motif",
			"Other	RO	Gene_Product_Is_Biomarker_Of",
			"Other	RO	Gene_Product_Is_Biomarker_Type",
			"Other	RO	Gene_Product_Is_Element_In_Pathway",
			"Other	RO	Gene_Product_Is_Physical_Part_Of",
			"Other	RO	Gene_Product_Malfunction_Associated_With_Disease",
			"Other	RO	Gene_Product_Plays_Role_In_Biological_Process",
			"Other	RO	Genomic_Mutation_Of",
			"Other	RO	Has_3_UTR",
			"Other	RO	Has_5_UTR",
			"Other	RO	has_access",
			"Other	RO	has_action",
			"Other	RO	has_active_ingredient",
			"Other	RO	has_additive",
			"Other	RO	has_affiliation",
			"Other	RO	Has_Allele",
			"Other	RO	has_allelic_variant",
			"Other	RO	Has_Amino_Acid_Variant",
			"Other	RO	Has_Aneuploidy_Addition",
			"Other	RO	Has_Aneuploidy_Deletion",
			"Other	RO	has_approach",
			"Other	RO	Has_Arm",
			"Other	RO	Has_Arm_Location",
			"Other	RO	has_associated_finding",
			"Other	RO	has_associated_morphology",
			"Other	RO	has_associated_procedure",
			"Other	RO	has_atmospheric_component",
			"Other	RO	Has_Band",
			"Other	RO	Has_Band_Location",
			"Other	RO	has_been_treated",
			"Other	RO	has_bioassay_data",
			"Other	RO	has_bioassays",
			"Other	RO	has_biomaterial_characteristics",
			"Other	RO	has_branch",
			"Other	RO	has_cancer_site",
			"Other	RO	has_category",
			"Other	RO	has_causative_agent",
			"Other	RO	Has_Centromere",
			"Other	RO	Has_CH3_Status",
			"Other	RO	has_chromosomal_aberration_classification",
			"Other	RO	Has_Chromosomal_Location",
			"Other	RO	has_citation",
			"Other	RO	has_clinical_course",
			"Other	RO	has_clinical_finding",
			"Other	RO	has_clinical_record",
			"Other	RO	has_clinical_treatment",
			"Other	RO	has_component",
			"Other	RO	has_compound",
			"Other	RO	Has_Constituent_Amino_Acid",
			"Other	RO	Has_Constituent_Element",
			"Other	RO	Has_Constituent_Protein",
			"Other	RO	Has_Constituent_Variant",
			"Other	RO	has_contraindicated_drug",
			"Other	RO	has_contraindication",
			"Other	RO	has_cubic_volume",
			"Other	RO	has_database",
			"Other	RO	has_database_entry_type",
			"Other	RO	has_definitional_manifestation",
			"Other	RO	has_degree",
			"Other	RO	has_diameter",
			"Other	RO	has_direct_device",
			"Other	RO	has_direct_morphology",
			"Other	RO	has_direct_procedure_site",
			"Other	RO	has_direct_substance",
			"Other	RO	has_disease_location",
			"Other	RO	has_disease_staging",
			"Other	RO	has_disease_state",
			"Other	RO	has_domain",
			"Other	RO	has_donor",
			"Other	RO	has_dose_form",
			"Other	RO	Has_Effect",
			"Other	RO	has_entry_combination",
			"Other	RO	has_episodicity",
			"Other	RO	has_evaluation",
			"Other	RO	Has_Exon",
			"Other	RO	has_experiment_design",
			"Other	RO	has_experiment_design_type",
			"Other	RO	has_experiment_factors",
			"Other	RO	has_factor_value",
			"Other	RO	has_factor_value_ontology_entry",
			"Other	RO	has_family_member",
			"Other	RO	has_family_relationship",
			"Other	RO	Has_Feature",
			"Other	RO	has_feature_shape",
			"Other	RO	has_fiducials",
			"Other	RO	has_finding_context",
			"Other	RO	has_finding_informer",
			"Other	RO	has_finding_method",
			"Other	RO	has_finding_site",
			"Other	RO	has_focus",
			"Other	RO	Has_Free_Acid_Or_Base_Form",
			"Other	RO	Has_Gene",
			"Other	RO	Has_Gene_Location",
			"Other	RO	Has_Gene_Product_Element",
			"Other	RO	Has_Genomic_Mutation",
			"Other	RO	has_hardware",
			"Other	RO	has_height",
			"Other	RO	has_host",
			"Other	RO	has_host_part",
			"Other	RO	has_image_format",
			"Other	RO	has_indirect_device",
			"Other	RO	has_indirect_morphology",
			"Other	RO	has_indirect_procedure_site",
			"Other	RO	has_individual",
			"Other	RO	has_individual_genetic_characteristics",
			"Other	RO	has_ingredient",
			"Other	RO	has_inheritance_type",
			"Other	RO	has_initial_time_point",
			"Other	RO	has_intent",
			"Other	RO	has_interpretation",
			"Other	RO	Has_Intron",
			"Other	RO	has_laterality",
			"Other	RO	has_length",
			"Other	RO	Has_Location",
			"Other	RO	has_location",
			"Other	RO	has_MAGE_description",
			"Other	RO	has_manifestation",
			"Other	RO	has_manufacturer",
			"Other	RO	has_mass",
			"Other	RO	Has_Maternal_Uniparental_Disomy",
			"Other	RO	has_maximum_measurement",
			"Other	RO	has_measurement",
			"Other	RO	has_measurement_method",
			"Other	RO	has_measurement_type",
			"Other	RO	has_mechanism_of_action",
			"Other	RO	has_member",
			"Other	RO	has_method",
			"Other	RO	Has_Mode_of_Inheritance",
			"Other	RO	has_node_value",
			"Other	RO	has_node_value_type",
			"Other	RO	has_nodes",
			"Other	RO	Has_Nucleotide_Repeat",
			"Other	RO	Has_Nucleotide_Variant",
			"Other	RO	has_nutrient_component",
			"Other	RO	has_occurrence",
			"Other	RO	has_organism_part",
			"Other	RO	has_owner",
			"Other	RO	has_parent_organization",
			"Other	RO	has_part_modified",
			"Other	RO	Has_Paternal_Uniparental_Disomy",
			"Other	RO	has_pathological_process",
			"Other	RO	has_performer",
			"Other	RO	has_pharmacokinetics",
			"Other	RO	Has_Physical_Part_Of_Anatomic_Structure",
			"Other	RO	has_physiologic_effect",
			"Other	RO	has_precise_ingredient",
			"Other	RO	has_prior_disease_state",
			"Other	RO	has_priority",
			"Other	RO	has_procedure_context",
			"Other	RO	has_procedure_device",
			"Other	RO	has_procedure_morphology",
			"Other	RO	has_procedure_site",
			"Other	RO	has_property",
			"Other	RO	has_property_set",
			"Other	RO	has_protocol",
			"Other	RO	has_providers",
			"Other	RO	has_reason_for_deprecation",
			"Other	RO	has_recipient_category",
			"Other	RO	has_result",
			"Other	RO	has_revision_status",
			"Other	RO	has_route_of_administration",
			"Other	RO	Has_RT_Product",
			"Other	RO	Has_Salt_Form",
			"Other	RO	has_scale",
			"Other	RO	has_scale_type",
			"Other	RO	Has_Segment",
			"Other	RO	has_severity",
			"Other	RO	has_software",
			"Other	RO	has_species",
			"Other	RO	has_specimen",
			"Other	RO	has_specimen_procedure",
			"Other	RO	has_specimen_source_identity",
			"Other	RO	has_specimen_source_morphology",
			"Other	RO	has_specimen_source_topography",
			"Other	RO	has_specimen_substance",
			"Other	RO	has_subject_relationship_context",
			"Other	RO	Has_Target",
			"Other	RO	Has_Telomere",
			"Other	RO	has_temporal_context",
			"Other	RO	has_test_result",
			"Other	RO	has_test_type",
			"Other	RO	has_time_aspect",
			"Other	RO	Has_Transcript",
			"Other	RO	has_treatment",
			"Other	RO	has_tributary",
			"Other	RO	has_type",
			"Other	RO	has_units",
			"Other	RO	has_URI",
			"Other	RO	has_value",
			"Other	RO	Human_Disease_Maps_To_EO_Disease",
			"Other	RO	Human_Sex_Determinant",
			"Other	RO	Imaged_Anatomy_Has_Procedure",
			"Other	RO	included_in",
			"Other	RO	includes",
			"Other	RO	indirect_device_of",
			"Other	RO	indirect_morphology_of",
			"Other	RO	indirect_procedure_site_of",
			"Other	RO	induced_by",
			"Other	RO	induces",
			"Other	RO	ingredient_of",
			"Other	RO	inheritance_type_of",
			"Other	RO	intent_of",
			"Other	RO	interpretation_of",
			"Other	RO	interprets",
			"Other	RO	Intron_of",
			"Other	RO	INV_Chromosomal_Structural_Variant",
			"Other	RO	INV_Deleted_Region_End_Band",
			"Other	RO	INV_Deleted_Region_Start_Band",
			"Other	RO	INV_Duplicated_Region_End_Band",
			"Other	RO	INV_Duplicated_Region_Start_Band",
			"Other	RO	INV_Human_Sex_Determinant",
			"Other	RO	INV_Inverted_Region_End_Band",
			"Other	RO	INV_Inverted_Region_End_Exon",
			"Other	RO	INV_Inverted_Region_End_Gene",
			"Other	RO	INV_Inverted_Region_End_UTR",
			"Other	RO	INV_Inverted_Region_Start_Band",
			"Other	RO	INV_Inverted_Region_Start_Exon",
			"Other	RO	INV_Inverted_Region_Start_Gene",
			"Other	RO	INV_Inverted_Region_Start_Intron",
			"Other	RO	INV_Involves",
			"Other	RO	INV_Isochromosome_Origin",
			"Other	RO	INV_Karyotype_Class",
			"Other	RO	INV_Source_Band",
			"Other	RO	INV_Source_Exon",
			"Other	RO	INV_Source_Gene",
			"Other	RO	INV_Source_Intron",
			"Other	RO	INV_Target_Band",
			"Other	RO	INV_Target_Exon",
			"Other	RO	INV_Target_Gene",
			"Other	RO	INV_Target_Intron",
			"Other	RO	inverse_has_additive",
			"Other	RO	inverse_has_affiliation",
			"Other	RO	inverse_has_atmospheric_component",
			"Other	RO	inverse_has_been_treated",
			"Other	RO	inverse_has_bioassay_data",
			"Other	RO	inverse_has_bioassays",
			"Other	RO	inverse_has_biomaterial_characteristics",
			"Other	RO	inverse_has_cancer_site",
			"Other	RO	inverse_has_category",
			"Other	RO	inverse_has_chromosomal_aberration_classification",
			"Other	RO	inverse_has_citation",
			"Other	RO	inverse_has_clinical_finding",
			"Other	RO	inverse_has_clinical_record",
			"Other	RO	inverse_has_clinical_treatment",
			"Other	RO	inverse_has_compound",
			"Other	RO	inverse_has_cubic_volume",
			"Other	RO	inverse_has_database",
			"Other	RO	inverse_has_database_entry_type",
			"Other	RO	inverse_has_diameter",
			"Other	RO	inverse_has_disease_location",
			"Other	RO	inverse_has_disease_staging",
			"Other	RO	inverse_has_disease_state",
			"Other	RO	inverse_has_donor",
			"Other	RO	inverse_has_experiment_design",
			"Other	RO	inverse_has_experiment_design_type",
			"Other	RO	inverse_has_experiment_factors",
			"Other	RO	inverse_has_factor_value",
			"Other	RO	inverse_has_factor_value_ontology_entry",
			"Other	RO	inverse_has_family_member",
			"Other	RO	inverse_has_family_relationship",
			"Other	RO	inverse_has_feature_shape",
			"Other	RO	inverse_has_fiducials",
			"Other	RO	inverse_has_hardware",
			"Other	RO	inverse_has_height",
			"Other	RO	inverse_has_host",
			"Other	RO	inverse_has_host_part",
			"Other	RO	inverse_has_image_format",
			"Other	RO	inverse_has_individual",
			"Other	RO	inverse_has_individual_genetic_characteristics",
			"Other	RO	inverse_has_initial_time_point",
			"Other	RO	inverse_has_length",
			"Other	RO	inverse_has_MAGE_description",
			"Other	RO	inverse_has_manufacturer",
			"Other	RO	inverse_has_mass",
			"Other	RO	inverse_has_maximum_measurement",
			"Other	RO	inverse_has_measurement_type",
			"Other	RO	inverse_has_node_value",
			"Other	RO	inverse_has_node_value_type",
			"Other	RO	inverse_has_nodes",
			"Other	RO	inverse_has_nutrient_component",
			"Other	RO	inverse_has_organism_part",
			"Other	RO	inverse_has_owner",
			"Other	RO	inverse_has_parent_organization",
			"Other	RO	inverse_has_part_modified",
			"Other	RO	inverse_has_performer",
			"Other	RO	inverse_has_prior_disease_state",
			"Other	RO	inverse_has_property_set",
			"Other	RO	inverse_has_protocol",
			"Other	RO	inverse_has_providers",
			"Other	RO	inverse_has_reason_for_deprecation",
			"Other	RO	inverse_has_software",
			"Other	RO	inverse_has_species",
			"Other	RO	inverse_has_test_result",
			"Other	RO	inverse_has_test_type",
			"Other	RO	inverse_has_treatment",
			"Other	RO	inverse_has_type",
			"Other	RO	inverse_has_units",
			"Other	RO	inverse_has_URI",
			"Other	RO	inverse_was_tested_for",
			"Other	RO	Inverted_Region_End_Band",
			"Other	RO	Inverted_Region_End_Exon",
			"Other	RO	Inverted_Region_End_Gene",
			"Other	RO	Inverted_Region_End_UTR",
			"Other	RO	Inverted_Region_Start_Band",
			"Other	RO	Inverted_Region_Start_Exon",
			"Other	RO	Inverted_Region_Start_Gene",
			"Other	RO	Inverted_Region_Start_Intron",
			"Other	RO	Involves",
			"Other	RO	Is_Abnormal_Cell_Of_Disease",
			"Other	RO	Is_Abnormality_Of_Gene",
			"Other	RO	Is_Abnormality_Of_Gene_Product",
			"Other	RO	Is_Associated_Anatomic_Site_Of",
			"Other	RO	Is_Associated_Anatomy_Of_Gene_Product",
			"Other	RO	Is_Associated_Disease_Of",
			"Other	RO	Is_Biochemical_Function_Of_Gene_Product",
			"Other	RO	Is_Chemical_Classification_Of_Gene_Product",
			"Other	RO	Is_Chromosomal_Location_Of_Gene",
			"Other	RO	Is_Component_Of_Chemotherapy_Regimen",
			"Other	RO	Is_Cytogenetic_Abnormality_Of_Disease",
			"Other	RO	Is_Finding_Of_Disease",
			"Other	RO	Is_Grade_Of_Disease",
			"Other	RO	is_interpreted_by",
			"Other	RO	Is_Location_Of",
			"Other	RO	Is_Location_Of_Anatomic_Structure",
			"Other	RO	Is_Location_Of_Biological_Process",
			"Other	RO	Is_Malfunction_Type_Of_Gene_Product",
			"Other	RO	Is_Marked_By_Gene_Product",
			"Other	RO	Is_Mechanism_Of_Action_Of_Chemical_Or_Drug",
			"Other	RO	Is_Metastatic_Anatomic_Site_Of_Disease",
			"Other	RO	Is_Molecular_Abnormality_Of_Disease",
			"Other	RO	Is_Normal_Cell_Origin_Of_Disease",
			"Other	RO	Is_Normal_Tissue_Origin_Of_Disease",
			"Other	RO	Is_Not_Abnormal_Cell_Of_Disease",
			"Other	RO	Is_Not_Cytogenetic_Abnormality_Of_Disease",
			"Other	RO	Is_Not_Finding_Of_Disease",
			"Other	RO	Is_Not_Molecular_Abnormality_Of_Disease",
			"Other	RO	Is_Not_Normal_Cell_Origin_Of_Disease",
			"Other	RO	Is_Not_Normal_Tissue_Origin_Of_Disease",
			"Other	RO	Is_Not_Primary_Anatomic_Site_Of_Disease",
			"Other	RO	Is_Organism_Source_Of_Gene_Product",
			"Other	RO	Is_Physical_Location_Of_Gene",
			"Other	RO	Is_Physiologic_Effect_Of_Chemical_Or_Drug",
			"Other	RO	Is_Primary_Anatomic_Site_Of_Disease",
			"Other	RO	Is_Property_Or_Attribute_Of_EO_Disease",
			"Other	RO	Is_Qualified_By",
			"Other	RO	Is_Related_To_Endogenous_Product",
			"Other	RO	Is_Stage_Of_Disease",
			"Other	RO	Is_Structural_Domain_Or_Motif_Of_Gene_Product",
			"Other	RO	Is_Target_Of_Agent",
			"Other	RO	Isochromosome_Origin",
			"Other	RO	Karyotype_Class",
			"Other	RO	Kind_Is_Domain_Of",
			"Other	RO	Kind_Is_Range_Of",
			"Other	RO	larger_than",
			"Other	RO	laterality_of",
			"Other	RO	location_of",
			"Other	RO	manifestation_of",
			"Other	RO	Maternal_Uniparental_Disomy_of",
			"Other	RO	May_Be_Abnormal_Cell_Of_Disease",
			"Other	RO	May_Be_Associated_Disease_Of_Disease",
			"Other	RO	May_Be_Cytogenetic_Abnormality_Of_Disease",
			"Other	RO	may_be_diagnosed_by",
			"Other	RO	May_Be_Finding_Of_Disease",
			"Other	RO	May_Be_Molecular_Abnormality_Of_Disease",
			"Other	RO	May_Be_Normal_Cell_Origin_Of_Disease",
			"Other	RO	may_be_prevented_by",
			"Other	RO	may_be_treated_by",
			"Other	RO	may_diagnose",
			"Other	RO	may_prevent",
			"Other	RO	may_treat",
			"Other	RO	measured_by",
			"Other	RO	measurement_method_of",
			"Other	RO	measurement_of",
			"Other	RO	measures",
			"Other	RO	mechanism_of_action_of",
			"Other	RO	member_of_cluster",
			"Other	RO	metabolic_site_of",
			"Other	RO	metabolized_by",
			"Other	RO	metabolizes",
			"Other	RO	method_of",
			"Other	RO	Mode_of_Inheritance_of",
			"Other	RO	Nucleotide_Repeat_of",
			"Other	RO	Nucleotide_Variant_of",
			"Other	RO	occurs_after",
			"Other	RO	occurs_before",
			"Other	RO	occurs_in",
			"Other	RO	Organism_Has_Gene",
			"Other	RO	Partially_Excised_Anatomy_Has_Procedure",
			"Other	RO	Partially_Excised_Anatomy_May_Have_Procedure",
			"Other	RO	Paternal_Uniparental_Disomy_of",
			"Other	RO	pathological_process_of",
			"Other	RO	Pathway_Has_Gene_Element",
			"Other	RO	pharmacokinetics_of",
			"Other	RO	physiologic_effect_of",
			"Other	RO	precise_ingredient_of",
			"Other	RO	priority_of",
			"Other	RO	procedure_context_of",
			"Other	RO	procedure_device_of",
			"Other	RO	Procedure_Has_Completely_Excised_Anatomy",
			"Other	RO	Procedure_Has_Excised_Anatomy",
			"Other	RO	Procedure_Has_Imaged_Anatomy",
			"Other	RO	Procedure_Has_Partially_Excised_Anatomy",
			"Other	RO	Procedure_Has_Target_Anatomy",
			"Other	RO	Procedure_May_Have_Completely_Excised_Anatomy",
			"Other	RO	Procedure_May_Have_Excised_Anatomy",
			"Other	RO	Procedure_May_Have_Partially_Excised_Anatomy",
			"Other	RO	procedure_morphology_of",
			"Other	RO	procedure_site_of",
			"Other	RO	Process_Altered_By_Allele",
			"Other	RO	Process_Includes_Biological_Process",
			"Other	RO	Process_Initiates_Biological_Process",
			"Other	RO	Process_Involves_Gene",
			"Other	RO	property_of",
			"Other	RO	Qualifier_Applies_To",
			"Other	RO	recipient_category_of",
			"Other	RO	reformulated_to",
			"Other	RO	reformulation_of",
			"Other	RO	Regimen_Has_Accepted_Use_For_Disease",
			"Other	RO	related_to",
			"Other	RO	result_of",
			"Other	RO	revision_status_of",
			"Other	RO	Role_Has_Domain",
			"Other	RO	Role_Has_Parent",
			"Other	RO	Role_Has_Range",
			"Other	RO	Role_Is_Parent_Of",
			"Other	RO	route_of_administration_of",
			"Other	RO	RT_Product_of",
			"Other	RO	scale_of",
			"Other	RO	scale_type_of",
			"Other	RO	Segment_of",
			"Other	RO	severity_of",
			"Other	RO	site_of_metabolism",
			"Other	RO	smaller_than",
			"Other	RO	Source_Band",
			"Other	RO	Source_Exon",
			"Other	RO	Source_Gene",
			"Other	RO	Source_Intron",
			"Other	RO	specimen_of",
			"Other	RO	specimen_procedure_of",
			"Other	RO	specimen_source_identity_of",
			"Other	RO	specimen_source_morphology_of",
			"Other	RO	specimen_source_topography_of",
			"Other	RO	specimen_substance_of",
			"Other	RO	subject_relationship_context_of",
			"Other	RO	Subset_Includes_Concept",
			"Other	RO	substance_used_by",
			"Other	RO	Target_Anatomy_Has_Procedure",
			"Other	RO	Target_Band",
			"Other	RO	Target_Exon",
			"Other	RO	Target_Gene",
			"Other	RO	Target_Intron",
			"Other	RO	Telomere_of",
			"Other	RO	temporal_context_of",
			"Other	RO	time_aspect_of",
			"Other	RO	Tissue_Is_Expression_Site_Of_Gene_Product",
			"Other	RO	Transcript_of",
			"Other	RO	treated_by",
			"Other	RO	treats",
			"Other	RO	tributary_of",
			"Other	RO	used_by",
			"Other	RO	uses",
			"Other	RO	uses_access_device",
			"Other	RO	uses_device",
			"Other	RO	uses_energy",
			"Other	RO	uses_substance",
			"Other	RO	value_of",
			"Other	RO	was_tested_for",
			"Other	RQ	[none]",
			"Other	RQ	alias_of",
			"Other	RQ	classified_as",
			"Other	RQ	classifies",
			"Other	RQ	clinically_similar",
			"Other	RQ	has_alias",
			"Other	RQ	has_outcome",
			"Other	RQ	mapped_from",
			"Other	RQ	mapped_to",
			"Other	RQ	outcome_of",
			"Other	RQ	see",
			"Other	RQ	see_from",
			"Other	RQ	use",
			"Other	RQ	used_for",
			"Other	SY	[none]",
			"Other	SY	alias_of",
			"Other	SY	british_form_of",
			"Other	SY	entry_version_of",
			"Other	SY	expanded_form_of",
			"Other	SY	has_alias",
			"Other	SY	has_british_form",
			"Other	SY	has_entry_version",
			"Other	SY	has_expanded_form",
			"Other	SY	has_multi_level_category",
			"Other	SY	has_permuted_term",
			"Other	SY	has_plain_text_form",
			"Other	SY	has_single_level_category",
			"Other	SY	has_sort_version",
			"Other	SY	mth_british_form_of",
			"Other	SY	mth_expanded_form_of",
			"Other	SY	mth_has_british_form",
			"Other	SY	mth_has_expanded_form",
			"Other	SY	mth_has_plain_text_form",
			"Other	SY	mth_has_xml_form",
			"Other	SY	mth_plain_text_form_of",
			"Other	SY	mth_xml_form_of",
			"Other	SY	permuted_term_of",
			"Other	SY	plain_text_form_of",
			"Other	SY	sort_version_of"
        });

	/**
	 * Constructor
	 */
	private Constants() {
		// Prevent class from being explicitly instantiated
	}

} // Class Constants
