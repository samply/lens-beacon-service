package de.samply.lens_beacon_service.entrytype.individuals;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class IndividualsNameOntologyMaps {
    // Maps the standard names for genders onto NCIT codes
    public static Map<String, String> genderNameNcit;
    static {
        genderNameNcit = new HashMap<String, String>();

        genderNameNcit.put("female", "NCIT:C16576");
        genderNameNcit.put("male", "NCIT:C20197");
    }

    // Maps the standard names for ethnicities onto NCIT codes
    public static Map<String, String> ethnicityNameNcit;
    static {
        ethnicityNameNcit = new HashMap<String, String>();

        ethnicityNameNcit.put("Irish", "NCIT:C43856");
        ethnicityNameNcit.put("Mixed", "NCIT:C67109");
        ethnicityNameNcit.put("White", "NCIT:C41261");
        ethnicityNameNcit.put("Indian", "NCIT:C67109");
        ethnicityNameNcit.put("Chinese", "NCIT:C41260");
        ethnicityNameNcit.put("African", "NCIT:C42331");
        ethnicityNameNcit.put("British", "NCIT:C41261");
        ethnicityNameNcit.put("Pakistani", "NCIT:C41260");
        ethnicityNameNcit.put("Caribbean", "NCIT:C77810");
        ethnicityNameNcit.put("Bangladeshi", "NCIT:C41260");
        ethnicityNameNcit.put("White and Asian", "NCIT:C67109");
        ethnicityNameNcit.put("Other ethnic group", "NCIT:C67109");
        ethnicityNameNcit.put("Asian or Asian British", "NCIT:C41260");
        ethnicityNameNcit.put("Black or Black British", "NCIT:C16352");
        ethnicityNameNcit.put("White and Black African", "NCIT:C67109");
        ethnicityNameNcit.put("White and Black Caribbean", "NCIT:C67109");
        ethnicityNameNcit.put("Any other Asian background", "NCIT:C67109");
        ethnicityNameNcit.put("Any other mixed background", "NCIT:C67109");
        ethnicityNameNcit.put("Any other Black background", "NCIT:C67109");
        ethnicityNameNcit.put("Any other white background", "NCIT:C67109");
    }

    // Maps the standard names for disease onto ICD19 codes
    public static Map<String, String> diseaseIcd10;
    static {
        diseaseIcd10 = new HashMap<String, String>();

        diseaseIcd10.put("acute bronchitis", "ICD10:J40");
        diseaseIcd10.put("agranulocytosis", "ICD10:D70");
        diseaseIcd10.put("asthma", "ICD10:J45");
        diseaseIcd10.put("bipolar affective disorder", "ICD10:F3181");
        diseaseIcd10.put("cardiomyopathy", "ICD10:I42");
        diseaseIcd10.put("dental caries", "ICD10:K02");
        diseaseIcd10.put("eating disorders", "ICD10:F50");
        diseaseIcd10.put("fibrosis and cirrhosis of liver", "ICD10:K74");
        diseaseIcd10.put("gastro-oesophageal reflux disease", "ICD10:K21");
        diseaseIcd10.put("haemorrhoids", "ICD10:K64");
        diseaseIcd10.put("influenza due to certain identified influenza virus", "ICD10:J11");
        diseaseIcd10.put("insulin-dependent diabetes mellitus", "ICD10:E10");
        diseaseIcd10.put("iron deficiency anaemia", "ICD10:D50");
        diseaseIcd10.put("multiple sclerosis", "ICD10:G35");
        diseaseIcd10.put("obesity", "ICD10:E66");
        diseaseIcd10.put("sarcoidosis", "ICD10:D86");
        diseaseIcd10.put("schizophrenia", "ICD10:F20");
        diseaseIcd10.put("thyroiditis", "ICD10:E06");
        diseaseIcd10.put("varicose veins of lower extremities", "ICD10:I83");
    }

    // Maps the standard names for geographic origin onto GAZ codes
    public static Map<String, String> geographicOriginGaz;
    static {
        geographicOriginGaz = new HashMap<String, String>();

        geographicOriginGaz.put("Wales", "GAZ:00002640");
        geographicOriginGaz.put("England", "GAZ:00002641");
        geographicOriginGaz.put("Scotland", "GAZ:00002639");
        geographicOriginGaz.put("Republic of Ireland", "GAZ:00004018");
        geographicOriginGaz.put("Northern Ireland", "GAZ:00002638");
    }
}
