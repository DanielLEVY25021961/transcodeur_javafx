/**
 * 
 */
package levy.daniel.application;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.IFournisseurFichiersService;
import levy.daniel.application.model.services.utilitaires.fournisseurfichiers.impl.FournisseurFichiersTraficService;
import levy.daniel.application.model.utilitaires.transcodeur.ITranscodeur;
import levy.daniel.application.model.utilitaires.transcodeur.impl.Transcodeur;

/**
 * CLASSE MainApplication :<br/>
 * .<br/>
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
 * @author daniel.levy Lévy
 * @version 1.0
 * @since 31 mai 2018
 *
 */
public final class MainApplication {

	// ************************ATTRIBUTS************************************/
	
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
	public static final Charset CHARSET_ANSI
		= Charset.forName("windows-1252");
	

	/**
	 * CHARSET_UTF8 : Charset :<br/>
	 * Charset.forName("UTF-8").<br/>
	 * Eight-bit Unicode (or UCS) Transformation Format.<br/> 
	 */
	public static final Charset CHARSET_UTF8 
		= Charset.forName("UTF-8");


	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG = LogFactory.getLog(MainApplication.class);
	

	// *************************METHODES************************************/
	
	 /**
	 * method CONSTRUCTEUR MainApplication() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	private MainApplication() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	
	
	
	/**
	 * .<br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 *
	 * @param pArgs : void :  .<br/>
	 * @throws Exception 
	 */
	public static void main(
			final String[] pArgs) throws Exception {
		
//		final Charset charsetEntree = PreferencesManager.getCharsetSupposeEntreeParDefaut();
//
//		System.out.println(charsetEntree.displayName());
//		
//		Thread.sleep(2000);
		
		transcoderMain();
		
	} // Fin de main(...)._________________________________________________


	private static void transcoderMain() throws Exception {
		
		final ITranscodeur transcodeur = new Transcodeur();
		
		final IFournisseurFichiersService fournisseurFichiers 
			= new FournisseurFichiersTraficService();
		
		final String pathBaseString = "D:\\Donnees\\eclipse\\eclipseworkspace_oxygen\\transcodeur_javafx\\src\\test\\resources\\fichiers\\Trafic_Histonat\\Trafic_2016\\";
		final File fichier = new File(pathBaseString + "0_Fichiers_originaux\\HITDIRO2016.txt");
		
		final File fichierANSI = fournisseurFichiers.fournirFileAnsi(fichier);
		final File fichierUtf8 = fournisseurFichiers.fournirFileUtf8(fichier);
		
		transcodeur.transcoder(
				fichier, CHARSET_ANSI, fichierANSI
					, CHARSET_UTF8, fichierUtf8);
	}

} // FIN DE LA CLASSE MainApplication.---------------------------------------
