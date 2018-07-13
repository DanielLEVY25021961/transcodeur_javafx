package levy.daniel.application.model.services.utilitaires.fournisseurfichiers;

import java.io.File;
import java.nio.charset.Charset;


/**
 * INTERFACE IFournisseurFichiersService :<br/>
 * Interface chargée de factoriser les comportements 
 * des FournisseurFichiersService.<br/>
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
public interface IFournisseurFichiersService {

	
	/**
	 * CHARSET_UTF8 : Charset :<br/>
	 * Charset.forName("UTF-8").<br/>
	 * Eight-bit Unicode (or UCS) Transformation Format.<br/> 
	 */
	Charset CHARSET_UTF8 = Charset.forName("UTF-8");


	/**
	 * CHARSET_ANSI : Charset :<br/>
	 * Charset.forName("windows-1252").<br/>
	 * ANSI, CP1252.<br/>
	 * 218 caractères imprimables.<br/>
	 * extension d’ISO-8859-1, qui rajoute quelques caractères: œ, € (euro), 
	 * guillemets anglais (« »), points de suspension (...)
	 * , signe «pour mille» (‰), 
	 * tirets cadratin (— = \u2014 en unicode ) et demi-cadratin (–), ...<br/>
	 */
	Charset CHARSET_ANSI = Charset.forName("windows-1252");


	/**
	 * CHARSET_IBM850 : Charset :<br/>
	 * Charset IBM-850.<br/>
	 * Cp850, MS-DOS Latin-1.<br/>
	 */
	Charset CHARSET_IBM850 = Charset.forName("IBM-850");


	/**
	 * Fournit un File abstrait (non enregistré sur HDD) 
	 * pour stocker le fichier dont toutes les lignes 
	 * seront encodées avec pCharset.<br/>
	 * Par exemple <code>
	 * "1_Fichiers_originaux_encodés_en_ANSI/HITDIRO2016_ANSI.txt"
	 * </code> pour pFile == HITDIRO2016.txt et pCharset == ANSI.<br/>
	 * <br/>
	 * - retourne null si pCharset == null.<br/>
	 * - retourne null si pFile == null.<br/>
	 * <br/>
	 *
	 * @param pCharset : Charset.<br/>
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : fichier abstrait (non enregistré sur HDD).<br/>
	 */
	File fournirFileAbstraitTotalementEncode(Charset pCharset, File pFile);
	

	/**
	 * Fournit un File en ANSI relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_ANSI.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_ANSI" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	File fournirFileAnsi(File pFile);


	/**
	 * Fournit un File en UTF-8 relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_UTF8.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_UTF8" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 *
	 * @param pFile : File.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	File fournirFileUtf8(File pFile);


	/**
	 * Fournit un File en pSuffixe relatif au pFile en entrée.<br/>
	 * Fournit un File abstrait (non existant sur le disque dur).<br/>
	 * <ul>
	 * <li>Le File fourni s'appelle pFile_pSuffixe.txt 
	 * et est situé dans un répertoire 
	 * "1_Fichiers_originaux_encodés_en_pSuffixe" 
	 * à côté du répertoire de pFile</li>
	 * </ul>
	 * - retourne null si pFile == null.<br/>
	 * - retourne null si pFile n'a pas de parent et grand-parent.<br/>
	 * <br/>
	 *
	 * @param pFile : File.<br/>
	 * @param pSuffixe : String.<br/>
	 * 
	 * @return : File : File relatif à l'entrée.<br/>
	 */
	File fournirFile(File pFile, String pSuffixe);


} // FIN DE L'INTERFACE IFournisseurFichiersService.-------------------------
