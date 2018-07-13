package levy.daniel.application.model.services.utilitaires.fournisseurfichiers.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.IFournisseurFichiersService;


/**
 * CLASSE FournisseurFichiersTraficService :<br/>
 * SERVICE chargé de fournir des Files abstraits (non enregistrés sur HDD) 
 * relatifs à un File en entrée.<br/>
 * <ul>
 * <li>par exemple <code>fournirFileUtf8(HITDIRO2016.txt)</code> 
 * retourne un File abstrait situé à 
 * "1_Fichiers_originaux_encodés_en_UTF8/HITDIRO2016_UTF8.txt" 
 * si le fichier original était situé à 
 * "0_Fichiers_originaux/HITDIRO2016.txt".</li>
 * </ul>
 * <br/>
 *
 * - Exemple d'utilisation :<br/>
 *<br/>
 * 
 * - Mots-clé :<br/>
 * <br/>
 *
 * - Dépendances :<br/>
 * <br/>
 *
 *
 * @author dan Lévy
 * @version 1.0
 * @since 3 juin 2018
 *
 */
public class FournisseurFichiersTraficService implements IFournisseurFichiersService {

	// ************************ATTRIBUTS************************************/
	

	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG 
	 = LogFactory.getLog(FournisseurFichiersTraficService.class);

	// *************************METHODES************************************/
	
	
	 /**
	 * method CONSTRUCTEUR FournisseurFichiersTraficService() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public FournisseurFichiersTraficService() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File fournirFileAbstraitTotalementEncode(
			final Charset pCharset
				, final File pFile) {
		
		/* retourne null si pCharset == null. */
		if (pCharset == null) {
			return null;
		}
		
		/* retourne null si pFile == null. */
		if (pFile == null) {
			return null;
		}
		
		File fileResultat = null;
		
		if (pCharset.equals(CHARSET_ANSI)) {
			fileResultat = fournirFileAnsi(pFile);
		} else if (pCharset.equals(CHARSET_UTF8)) {
			fileResultat = fournirFileUtf8(pFile);
		} else {
			fileResultat 
				= fournirFile(pFile
					, pCharset.displayName());
		}
		
		return fileResultat;
		
	} // Fin de fournirFileAbstraitTotalementEncode(...).__________________
	
	
	
	/**
	 * Retourne un Suffixe relatif à un Charset 
	 * pour les noms de fichier.<br/>
	 * <ul>
	 * Par exemple :
	 * <li><code>fournirSuffixe(CHARSET_UTF8)</code> retourne "UTF8".</li>
	 * <li><code>fournirSuffixe(CHARSET_ANSI)</code> retourne "ANSI".</li>
	 * <li><code>fournirSuffixe(CHARSET_IBM850)</code> retourne "IBM850".</li>
	 * </ul>
	 *
	 * @param pCharset : Charset.<br/>
	 * @return : String : Suffixe pour les noms de fichiers.<br/>
	 */
	private String fournirSuffixe(
			final Charset pCharset) {
		
		/* retourne null si pCharset == null. */
		if (pCharset == null) {
			return null;
		}
		
		if (pCharset.equals(CHARSET_UTF8)) {
			return "UTF8";
		} else if (pCharset.equals(CHARSET_ANSI)) {
			return "ANSI";
		} else if (pCharset.equals(CHARSET_IBM850)) {
			return "IBM850";
		} else {
			return pCharset.displayName(Locale.getDefault());
		}
				
	} // Fin de fournirSuffixe(...)._______________________________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File fournirFileAnsi(
			final File pFile) {
		
		return this.fournirFile(pFile, "ANSI");
		
	} // Fin de fournirFileAnsi(...).______________________________________
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File fournirFileUtf8(
			final File pFile) {
		
		return this.fournirFile(pFile, "UTF8");
		
	} // Fin de fournirFileUtf8(...).______________________________________

	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public File fournirFile(
			final File pFile
				, final String pSuffixe) {
		
		/* retourne null si pFile == null. */
		if (pFile == null) {
			return null;
		}

		final Path pathFile = pFile.toPath();
		final Path pathFileParent = pathFile.getParent();
		
		if (pathFileParent == null) {
			return null;
		}
		
		final Path pathFileGrandParent = pathFileParent.getParent();
		
		if (pathFileGrandParent == null) {
			return null;
		}
		
		final Path resultat = pathFileGrandParent.resolve(
				"1_Fichiers_originaux_encodés_en_" + pSuffixe +"/" 
						+ nommerFichier(pFile, pSuffixe));
		
		return resultat.toFile();
		
	} // Fin de fournirFile(...).__________________________________________
	

	
	/**
	 * Fabrique un nom de fichier en suffixant son nom.<br/>
	 * <ul>
	 * <li><code>nommerFichier(fileHITDIRO2016Txt, ANSI)</code> 
	 * retourne HITDIRO2016_ANSI.txt</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * @param pSuffixe : String : suffixe à rajouter entre le nom et l'extensin.<br/>
	 * 
	 * @return : String : nom pour un File.<br/>
	 */
	private String nommerFichier(
			final File pFile
				, final String pSuffixe) {
		
		if (pFile == null) {
			return null;
		}
		
		String resultat = null;
		
		final String nomFichier = pFile.getName();
		
		final String[] elements = StringUtils.split(nomFichier, '.');
		
		String nom = null;
		String extension = null;
		
		/* pas d'extension. */
		if (elements.length == 1) {
			nom = elements[0];
		} else {
			
			final int taille = elements.length;			
			extension = elements[taille - 1];
			nom = StringUtils.removeEnd(nomFichier, '.' + extension);
		}
		
		if (pSuffixe != null) {
			if (extension != null) {
				resultat = nom + '_' + pSuffixe + '.' + extension;
			} else {
				resultat = nom + '_' + pSuffixe;
			}
			
		} else {
			resultat = nomFichier;
		}
		
		return resultat;
		
	} // Fin de nommerFichier(...).________________________________________
	
	

} // FIN DE LA CLASSE FournisseurFichiersTraficService.-----------------------------
