package levy.daniel.application.model.utilitaires.date;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;


/**
 * CLASSE DateUtilTest :<br/>
 * Test JUnit de la classe 
 * {@link levy.daniel.application.model.utilitaires.date.DateUtil}}.<br/>
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
 * @since 18 mai 2018
 *
 */
public class DateUtilTest {

	// ************************ATTRIBUTS************************************/
	
	/**
	 * AFFICHAGE_GENERAL : Boolean :<br/>
	 * Boolean qui commande l'affichage pour tous les tests.<br/>
	 */
	public static final Boolean AFFICHAGE_GENERAL = true;

	/**
	 * UNUSED : String :<br/>
	 * "unused".<br/>
	 */
	private static final String UNUSED ="unused";

	/**
	 * SEP_MOINS_ARERE : String :<br/>
	 * " - ".<br/>
	 */
	public static final String SEP_MOINS_ARERE = " - ";
	
	/**
	 * ETOILES : String :<br/>
	 * " ****************** ".<br/>
	 */
	public static final String ETOILES 
		= " ****************** ";
	
	/**
	 * CLASSE_DATEUTILTEST : String :<br/>
	 * "CLASSE DateUtilTest".<br/>
	 */
	public static final String CLASSE_DATEUTILTEST 
		= "CLASSE DateUtilTest";

	
	/**
	 * METHOD_TEST_FORMAT : String :<br/>
	 * "Méthode testFormat()".<br/>
	 */
	public static final String METHOD_TEST_FORMAT 
		= "Méthode testFormat()";

	/**
	 * METHOD_TEST_PARSE : String :<br/>
	 * "Méthode testParse()".<br/>
	 */
	public static final String METHOD_TEST_PARSE 
		= "Méthode testParse()";
	
	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG = LogFactory.getLog(DateUtilTest.class);
	

	// *************************METHODES************************************/

	
	 /**
	 * method CONSTRUCTEUR DateUtilTest() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public DateUtilTest() {
		super();
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________
	

	
	/**
	 * teste la méthode 
	 * {@link DateUtil#format(java.time.LocalDate)}.<br/>
	 * garantit que :
	 * <ul>
	 * <li>format(null) retourne null.</li>
	 * <li>format(LocalDate valide) retourne une chaine 
	 * de caractères valide.</li>
	 * </ul>
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testFormat() {
				
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************
		
		/* format(null) retourne null. */
		final String dateStringNull = DateUtil.format(null);
		
		assertNull(
				"DateUtil.format(null) doit retourner null : "
					, dateStringNull);
		
		/* format(date valide) retourne une chaine de caractères valide. */
		final String dateStringReel 
			= DateUtil.format(LocalDate.of(1961, 2, 25));
		
		assertTrue(
				"La date String doit être valide : "
					, DateUtil.validDate(dateStringReel));
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			
			System.out.println();
			System.out.print(ETOILES);
			System.out.println(CLASSE_DATEUTILTEST + SEP_MOINS_ARERE 
					+ METHOD_TEST_FORMAT + ETOILES);
			System.out.println("LocalDate formatée : " + dateStringReel);
		}
				
	} // Fin de testFormat().______________________________________________


	
	/**
	 * teste la méthode 
	 * {@link DateUtil#parse(String)}.<br/>
	 * garantit que :
	 * <ul>
	 * <li>parse(null) retourne null.</li>
	 * <li>parse(dateString invalide) retourne null.</li>
	 * <li>parse(dateString valide) retourne une LocalDate valide.</li>
	 * </ul>
	 */
	@SuppressWarnings(UNUSED)
	@Test
	public void testParse() {
		
		// **********************************
		// AFFICHAGE DANS LE TEST ou NON
		final boolean affichage = false;
		// **********************************

		/* parse(null) retourne null. */
		final String stringNull = null;
		LocalDate dateNull = null;
		dateNull = DateUtil.parse(stringNull);
				
		assertNull(
				"DateUtil.parse(stringNull) == null : "
					, dateNull);

		
		/* parse(dateString invalide) retourne null. */
		final String stringInvalide = "25/02/1961";
		LocalDate dateInvalide = null;
		dateInvalide = DateUtil.parse(stringInvalide);
		
		assertNull(
				"DateUtil.parse(stringInvalide) == null : "
					, dateInvalide);
		
		/* parse(dateString valide) retourne LocalDate valide. */
		final String stringValide = "25 février 1961";
		LocalDate dateValide = null;
		dateValide = DateUtil.parse(stringValide);
		
		assertNotNull(
				"DateUtil.parse(stringValide) == LocalDateValide : "
					, dateValide);
		
		/* AFFICHAGE A LA CONSOLE. */
		if (AFFICHAGE_GENERAL && affichage) {
			
			System.out.println();
			System.out.print(ETOILES);
			System.out.println(CLASSE_DATEUTILTEST + SEP_MOINS_ARERE 
					+ METHOD_TEST_PARSE + ETOILES);
			System.out.println("LocalDate parsée : " + dateValide.toString());
			
		}
		
	} // Fin de testParse()._______________________________________________
	
	
	
} // FIN DE LA CLASSE DateUtilTest.------------------------------------------
