/**
 * 
 */
package levy.daniel.application.apptechnic.configurationmanagers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * CLASSE PreferencesManagerTest :<br/>
 * Test JUnit de la classe PreferencesManager.<br/>
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
 * @since 29 mai 2018
 *
 */
public class PreferencesManagerTest {

	// ************************ATTRIBUTS************************************/
	
	/**
	 * AFFICHAGE_GENERAL : Boolean :<br/>
	 * Boolean qui commande l'affichage pour tous les tests.<br/>
	 */
	public static final Boolean AFFICHAGE_GENERAL = true;

	/**
	 * KEY_TEST : String :<br/>
	 * "key.test".<br/>
	 */
	public static final String KEY_TEST = "key.test";
	
	/**
	 * VALUE_TEST : String :<br/>
	 * "test".<br/>
	 */
	public static final String VALUE_TEST = "test";
	
	/**
	 * KEY_CHARSET_SUPPOSE : String :<br/>
	 * "encodage.entree.suppose".<br/>
	 */
	public static final String KEY_CHARSET_SUPPOSE 
		= "encodage.entree.suppose";

	/**
	 * KEY_REP_ENTREE : String :<br/>
	 * "repertoire.entree".<br/>
	 */
	public static final String KEY_REP_ENTREE 
		= "repertoire.entree";

	/**
	 * TAILLE_INITIALE : String :<br/>
	 * "taille initiale : ".<br/>
	 */
	public static final String TAILLE_INITIALE 
		= "taille initiale : ";
	
	/**
	 * TAILLE_FINALE : String :<br/>
	 * "taille finale : ".<br/>
	 */
	public static final String TAILLE_FINALE 
		= "taille finale : ";
	
	/**
	 * INITIAL : String :<br/>
	 * "INITIAL : \n".<br/>
	 */
	public static final String INITIAL 
		= "PROPERTIES INITIAL : \n";
	
	/**
	 * FINAL : String :<br/>
	 * "FINAL : \n".<br/>
	 */
	public static final String FINAL 
		= "PROPERTIES FINAL : \n";
	
	/**
	 * UNUSED : String :<br/>
	 * "unused".<br/>
	 */
	public static final String UNUSED 
		= "unused";
	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG 
		= LogFactory.getLog(PreferencesManagerTest.class);
	

	// *************************METHODES************************************/
	

	 /**
	 * method CONSTRUCTEUR PreferencesManagerTest() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 */
	public PreferencesManagerTest() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	
	
	
	/**
	 * Teste la méthode creerOuModifierProperty(...).<br/>
	 * @throws Exception 
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testCreerOuModifier() throws Exception {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
				
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE PreferencesManagerTest - méthode testCreerOuModifier() ********** ");
			System.out.println(INITIAL + PreferencesManager.afficherPreferences());
		}
		
		final Properties preferences 
			= PreferencesManager.getPreferences();
		
		final int tailleInitiale = preferences.size();
		
		PreferencesManager.creerOuModifierProperty(KEY_TEST, VALUE_TEST);
		
		final int tailleFinale = preferences.size();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println(TAILLE_INITIALE + tailleInitiale);
			System.out.println(TAILLE_FINALE + tailleFinale);
			System.out.println();
		}
		
		assertEquals(
				"Il doit y avoir une property de plus : "
					, tailleInitiale + 1
						, tailleFinale);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println(FINAL + PreferencesManager.afficherPreferences());
		}

		PreferencesManager.creerOuModifierProperty(KEY_TEST, "test_modifié");
		
		final int tailleFinale2 = preferences.size();
		
		assertEquals(
				"Il doit y avoir le même nombre de properties : "
					, tailleFinale
						, tailleFinale2);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println(TAILLE_FINALE + tailleFinale);
			System.out.println("taille finale APRES MODIFICATION : " + tailleFinale2);
			System.out.println();
			System.out.println("PROPERTIES FINAL APRES MODIFICATION : \n" + PreferencesManager.afficherPreferences());
		}
		
		PreferencesManager.retirerProperty(KEY_TEST);
		
	} // Fin de testCreerOuModifier()._____________________________________


	
	/**
	 * Teste la méthode retirerProperty(...).<br/>
	 * @throws Exception 
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testRetirer() throws Exception {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
				
		/* AFFICHAGE A LA CONSOLE. */		
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE PreferencesManagerTest - méthode testRetirer() ********** ");
			System.out.println(INITIAL + PreferencesManager.afficherPreferences());
		}
		
		final Properties preferences 
		= PreferencesManager.getPreferences();
	
		final int tailleInitiale = preferences.size();
		
		PreferencesManager.creerOuModifierProperty("key.test.retirer", VALUE_TEST);
		
		final int tailleFinale = preferences.size();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println(TAILLE_INITIALE + tailleInitiale);
			System.out.println(TAILLE_FINALE + tailleFinale);
			System.out.println();
		}
		
		assertEquals(
				"Il doit y avoir une property de plus : "
					, tailleInitiale + 1
						, tailleFinale);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println(FINAL + PreferencesManager.afficherPreferences());
		}

		PreferencesManager.retirerProperty("key.test.retirer");
		
		final int tailleFinale2 = preferences.size();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("PROPERTIES FINAL APRES RETRAIT : \n" 
					+ PreferencesManager.afficherPreferences());
		}
		
		assertEquals(
				"Il doit y avoir le même nombre de properties qu'initialement : "
					, tailleInitiale
						, tailleFinale2);
		
		
		
	} // Fin de testRetirer()._____________________________________________
	

	
	/**
	 * Teste la méthodes getCharsetSupposeEntreeParDefaut()(...).<br/>
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testGetCharsetSupposeEntreeParDefaut() throws Exception {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
				
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE PreferencesManagerTest - méthode testGetCharsetSupposeEntreeParDefaut() ********** ");
			System.out.println(INITIAL + PreferencesManager.afficherPreferences());
		}
		

		final Charset charset 
			= PreferencesManager.getCharsetSupposeEntreeParDefaut();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("CHARSET : " + charset.displayName());
		}
		
		assertNotNull("le charset ne doit pas être null : "
				, charset);
		
		/* Changement de Charset. */
		PreferencesManager
			.setCharsetSupposeEntreeParDefaut(Charset.forName("Windows-1252"));
		
		final Charset charsetModifie 
			= PreferencesManager.getCharsetSupposeEntreeParDefaut();
	
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println();
			System.out.println("PROPERTIES après setCharsetSupposeEntreeParDefaut(...) : \n" + PreferencesManager.afficherPreferences());
			System.out.println("CHARSET MODIFIE : " + charsetModifie.displayName());
		}
		
		assertNotNull("le charset modifie ne doit pas être null : "
				, charsetModifie);
		
	} // Fin de testGetCharsetSupposeEntreeParDefaut().____________________
	
	
	
	/**
	 * Teste la méthode getRepEntreeParDefaut().<br/>
	 * <ul>
	 * <li></li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testGetRepEntreeParDefaut() throws Exception {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
				
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE PreferencesManagerTest - méthode testGet() ********** ");
			System.out.println(INITIAL + PreferencesManager.afficherPreferences());
		}

		final File repertoire 
			= PreferencesManager.getRepFichiersEntreeParDefaut();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			if (repertoire != null) {
				System.out.println("REPERTOIRE ENTREE : " + repertoire.getAbsolutePath());
			} else {
				System.out.println("REPERTOIRE ENTREE : null");
			}			
		}
		
		assertNotNull("le répertoire d'entrée ne doit pas être null : "
				, repertoire);
		
	} // Fin de testGetRepEntreeParDefaut()._______________________________
	

	
	/**
	 * Teste la méthodes testGetCharsetSortieParDefaut()(...).<br/>
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testGetCharsetSortieParDefaut() throws Exception {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
				
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("********** CLASSE PreferencesManagerTest - méthode testGetCharsetSortieParDefaut() ********** ");
			System.out.println(INITIAL + PreferencesManager.afficherPreferences());
		}
		

		final Charset charset 
			= PreferencesManager.getCharsetSortieParDefaut();
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println("CHARSET : " + charset.displayName());
		}
		
		assertNotNull("le charset ne doit pas être null : "
				, charset);
		
		/* Changement de Charset. */
		PreferencesManager
			.setCharsetSortieParDefaut(Charset.forName("Windows-1252"));
		
		final Charset charsetModifie 
			= PreferencesManager.getCharsetSortieParDefaut();
	
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			System.out.println();
			System.out.println("PROPERTIES après setCharsetSortieParDefaut(...) : \n" + PreferencesManager.afficherPreferences());
			System.out.println("CHARSET MODIFIE : " + charsetModifie.displayName());
		}
		
		assertNotNull("le charset modifie ne doit pas être null : "
				, charsetModifie);
		
	} // Fin de testGetCharsetSupposeEntreeParDefaut().____________________
	


} // FIN DE LA CLASSE PreferencesManagerTest.--------------------------------
