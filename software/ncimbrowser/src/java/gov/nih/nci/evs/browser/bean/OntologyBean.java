package gov.nih.nci.evs.browser.bean;

import gov.nih.nci.evs.browser.utils.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import javax.faces.model.SelectItem;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.Constructors;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;


public class OntologyBean {

	private static List _rela_list = null;
	private static List _association_name_list = null;
	private static List _property_name_list = null;
	private static List _property_type_list = null;
	private static List _source_list = null;

	private static Vector _rela_vec = null;
	private static Vector _association_name_vec = null;
	private static Vector _property_name_vec = null;
	private static Vector _property_type_vec = null;
	private static Vector _source_vec = null;

	private static String codingSchemeName = "NCI Metathesaurus";

//	NameAndValueList associationNameAndValueList = null;


    public static List getRELAList() {
		if (_rela_list != null) return _rela_list;
		_rela_list = new ArrayList();
		if (_rela_vec == null) {
			_rela_vec = getRELAs(codingSchemeName, null);
		}
		_rela_list.add(new SelectItem(" ", " "));
		for (int k=0; k<_rela_vec.size(); k++) {
			String value = (String) _rela_vec.elementAt(k);
			_rela_list.add(new SelectItem(value, value));
		}
		return _rela_list;
	}

    public static Vector getAssociationNames() {
		if (_association_name_vec != null) {
			return _association_name_vec;
		}
		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		_association_name_vec = getSupportedAssociationNames(cs);
        return _association_name_vec;
	}


    public static List getAssociationNameList() {
		if (_association_name_list != null) return _association_name_list;
		_association_name_list = new ArrayList();
		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		if (_association_name_vec == null) {
			_association_name_vec = getSupportedAssociationNames(cs);
		}
		//Vector<String> associations = getSupportedAssociationNames(cs);

		for (int k=0; k<_association_name_vec.size(); k++) {
			String value = (String) _association_name_vec.elementAt(k);
			_association_name_list.add(new SelectItem(value, value));
		}
		return _association_name_list;
	}

    public static List getPropertyNameList() {
		if (_property_name_list != null) return _property_name_list;
		_property_name_list = new ArrayList();
		_property_name_list.add(new SelectItem(" ", " "));

		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		Vector<String> properties = getSupportedPropertyNames(cs);
		for (int k=0; k<properties.size(); k++) {
			String value = (String) properties.elementAt(k);
			_property_name_list.add(new SelectItem(value, value));
		}
		return _property_name_list;
	}

	public static List getSourceList() {
		if (_source_list != null) return _source_list;
		_source_list = new ArrayList();
		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		_source_list.add(new SelectItem("ALL", "ALL"));

		Vector<String> sources = getSupportedSources(cs);
		for (int k=0; k<sources.size(); k++) {
			String value = (String) sources.elementAt(k);
			_source_list.add(new SelectItem(value, value));
		}
		return _source_list;
	}


    public static List getPropertyTypeList() {
		if (_property_type_list != null) return _property_type_list;
		_property_type_list = new ArrayList();
		_property_type_list.add(new SelectItem("ALL", "ALL"));

		Vector<String> propertytypes = getSupportedPropertyTypes();
		for (int k=0; k<propertytypes.size(); k++) {
			String value = (String) propertytypes.elementAt(k);
			_property_type_list.add(new SelectItem(value, value));
		}
		return _property_type_list;
	}

 	public static Vector getRELAs() {
		if (_rela_vec != null) return _rela_vec;
		return getRELAs(codingSchemeName, null);
	}


	public static Vector getRELAs(String scheme, String version) {
		Vector v = new Vector();
		HashSet hset = new HashSet();
		LexBIGService lbs = RemoteServerUtil.createLexBIGService(false);
		LexBIGServiceMetadata lbsm = null;
		try {
			lbsm = lbs.getServiceMetadata();
			/*
			System.out.println("Loaded Metadata:");
			for(AbsoluteCodingSchemeVersionReference ref : lbsm.listCodingSchemes().getAbsoluteCodingSchemeVersionReference()){
				System.out.println("Name: " + ref.getCodingSchemeURN());
				System.out.println("	Version: " + ref.getCodingSchemeVersion());
			}
			*/

			lbsm = lbsm.restrictToCodingScheme(Constructors.createAbsoluteCodingSchemeVersionReference(scheme, version));

			MetadataPropertyList mdpl = lbsm.resolve();
			Set<String> relas = new HashSet<String>();
			int rela_count = 0;
			for(int i=0;i<mdpl.getMetadataPropertyCount();i++){
				MetadataProperty prop = mdpl.getMetadataProperty(i);
				if(prop.getName().equals("dockey") && prop.getValue().equals("RELA")){
					i++;
					rela_count++;
					prop = mdpl.getMetadataProperty(i);
					if (!hset.contains(prop.getValue())) {
						relas.add(prop.getValue());
						v.add(prop.getValue());
						hset.add(prop.getValue());
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		hset.clear();
		return SortUtils.quickSort(v);
	}

///////////////////////
// Convenience Methods
///////////////////////

    private static CodingScheme getCodingScheme(String codingScheme, String version)
    {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		CodingScheme cs = null;
        try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			try {
			    cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
			} catch (Exception ex2) {
				cs = null;
			}

	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return cs;
	}


    private static Vector getSupportedEntityType(CodingScheme cs)
    {
		if (cs == null) return null;
		Vector v = new Vector();
        try {
			SupportedEntityType[] types = cs.getMappings().getSupportedEntityType();
			for (int i=0; i<types.length; i++)
			{
				v.add(types[i].getLocalId());
			}
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return SortUtils.quickSort(v);
	}

    private static Vector getSupportedPropertyQualifier(CodingScheme cs)
    {
		if (cs == null) return null;
		Vector v = new Vector();
        try {
			SupportedPropertyQualifier[] qualifiers = cs.getMappings().getSupportedPropertyQualifier();
			for (int i=0; i<qualifiers.length; i++)
			{
				v.add(qualifiers[i].getLocalId());
			}
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return SortUtils.quickSort(v);
	}

    public static Vector getSupportedSources() {
		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		return getSupportedSources(cs);
	}


    private static Vector getSupportedSources(CodingScheme cs)
    {
		if (cs == null) return null;
		Vector v = new Vector();
        try {
			SupportedSource[] sources = cs.getMappings().getSupportedSource();
			for (int i=0; i<sources.length; i++)
			{
				v.add(sources[i].getLocalId());
			}
	    } catch (Exception ex) {
			ex.printStackTrace();
		}
		return SortUtils.quickSort(v);
	}


    private static Vector getSupportedPropertyTypes()
    {
		Vector v = new Vector();
		v.add("PRESENTATION");
		v.add("DEFINITION");
		v.add("COMMENT");
		v.add("GENERIC");
		return v;//SortUtils.quickSort(v);
	}

    public static Vector<SupportedProperty> getSupportedProperties(CodingScheme cs)
	{
		if (cs == null) return null;
        Vector<SupportedProperty> v = new Vector<SupportedProperty>();
	    SupportedProperty[] properties = cs.getMappings().getSupportedProperty();
		for (int i=0; i<properties.length; i++)
		{
		     SupportedProperty sp = (SupportedProperty) properties[i];
		     v.add(sp);
		}
        return SortUtils.quickSort(v);
	}

    public static Vector<String> getSupportedPropertyNames(CodingScheme cs)
	{
        Vector w = getSupportedProperties(cs);
		if (w == null) return null;

        Vector<String> v = new Vector<String>();
		for (int i=0; i<w.size(); i++)
		{
		     SupportedProperty sp = (SupportedProperty) w.elementAt(i);
		     v.add(sp.getLocalId());
		}
        return SortUtils.quickSort(v);
	}


	public static Vector<String> getSupportedAssociationQualifier(CodingScheme cs)
	{
        Vector<String> v = new Vector<String>();
        try {
			org.LexGrid.naming.SupportedAssociationQualifier[] supportedAssociationQualifiers
			    = cs.getMappings().getSupportedAssociationQualifier();
			if (supportedAssociationQualifiers == null) return null;
			for (int i=0; i<supportedAssociationQualifiers.length; i++)
			{
				SupportedAssociationQualifier q = supportedAssociationQualifiers[i];
				v.add(q.getLocalId());
			}
	    } catch (Exception e) {
			return null;
		}
		return SortUtils.quickSort(v);
	}

    public static Vector<String> getSupportedAssociationNames() {
		CodingScheme cs = getCodingScheme(codingSchemeName, null);
		return getSupportedAssociationNames( cs );
	}


    public static Vector<String> getSupportedAssociationNames(CodingScheme cs )
	{
		if (cs == null) return null;
        Vector<String> v = new Vector<String>();
	    SupportedAssociation[] assos = cs.getMappings().getSupportedAssociation();
		for (int i=0; i<assos.length; i++)
		{
		     SupportedAssociation sa = (SupportedAssociation) assos[i];
		     v.add(sa.getLocalId());
		}
        return SortUtils.quickSort(v);
	}


    public static Vector getAssociationCodesByNames(String codingScheme, String version, Vector associations) {
		LexBIGServiceConvenienceMethodsImpl lbscm = null;
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		Vector w = new Vector();

		try {
			LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
			lbscm = (LexBIGServiceConvenienceMethodsImpl)
				 lbSvc.getGenericExtension("LexBIGServiceConvenienceMethods");
			lbscm.setLexBIGService(lbSvc);

			for (int i=0; i<associations.size(); i++) {
				String entityCode = (String) associations.elementAt(i);
				try {
					String name = lbscm.getAssociationNameFromAssociationCode(codingScheme, versionOrTag, entityCode);
					w.add(name);
				} catch (Exception e) {
					w.add("<NOT ASSIGNED>");
				}
			}
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return SortUtils.quickSort(w);
	}


	public static void dumpVector(String label, Vector v) {
		System.out.println("\n" + label);
		if (v == null) return;
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + "): " + t);
		}
	}


	public static void main(String[] args) throws Exception {

		String scheme = "NCI Metathesaurus";
		String version = null;

		Vector rela_vec = getRELAs(scheme, version);
		dumpVector("Supported RELAs", rela_vec);

		CodingScheme cs = getCodingScheme(scheme, version);
		Vector<String> properties = getSupportedPropertyNames(cs);
		dumpVector("Supported Properties", properties);

		Vector<String> associations = getSupportedAssociationNames(cs);
		dumpVector("Supported Association Names", associations);

	}
}
