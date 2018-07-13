/**
 * 
 */
package levy.daniel.application.controllers.desktop.utilitaires.transcodeur.impl;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * CLASSE TranscodeurController :<br/>
 * CONTROLLER pour les Transcodeurs.<br/>
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
public class TranscodeurController {

	// ************************ATTRIBUTS************************************/

	/**
	 * charsetSupposeEntree : Charset :<br/>
	 * Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 */
	private Charset charsetSupposeEntree;
	
	/**
	 * fileATranscoder : File :<br/>
	 * Fichier à transcoder en charsetSortie.<br/>
	 */
	private File fileATranscoder;

	/**
	 * charsetSortie : Charset :<br/>
	 * Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 */
	private Charset charsetSortie;
	
	/**
	 * fichierSortie : File :<br/>
	 * fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 */
	private File fichierSortie;
	
	/**
	 * LOG : Log : 
	 * Logger pour Log4j (utilisant commons-logging).
	 */
	private static final Log LOG 
		= LogFactory.getLog(TranscodeurController.class);

	// *************************METHODES************************************/
	
	 /**
	 * method CONSTRUCTEUR TranscodeurController() :<br/>
	 * CONSTRUCTEUR D'ARITE NULLE.<br/>
	 * <br/>
	 */
	public TranscodeurController() {
		this(null, null, null, null);
	} // Fin de CONSTRUCTEUR D'ARITE NULLE.________________________________


	
	 /**
	 * method CONSTRUCTEUR TranscodeurController() :<br/>
	 * CONSTRUCTEUR COMPLET.<br/>
	 * <br/>
	 *
	 * @param pCharsetSupposeEntree : Charset : 
	 * Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/> 
	 * @param pFileATranscoder : File : 
	 * Fichier à transcoder en charsetSortie.<br/>
	 * @param pCharsetSortie : Charset : 
	 * Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 * @param pFichierSortie : File : 
	 * fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 */
	public TranscodeurController(
			final Charset pCharsetSupposeEntree
				, final File pFileATranscoder
					, final Charset pCharsetSortie
						, final File pFichierSortie) {
		
		super();
		
		this.charsetSupposeEntree = pCharsetSupposeEntree;
		this.fileATranscoder = pFileATranscoder;
		this.charsetSortie = pCharsetSortie;
		this.fichierSortie = pFichierSortie;
		
	} // Fin de CONSTRUCTEUR COMPLET.______________________________________



	/**
	 * Transcode pFileATranscoder en pCharsetSortie.<br/>
	 * <br/>
	 * <ul>
	 * <li>Retourne le fichier transcodé resultat pFichierSortie.</li>
	 * </ul>
	 *
	 * @param pCharsetSupposeEntree
	 * @param pFileATranscoder
	 * @param pCharsetSortie
	 * @param pFichierSortie
	 * 
	 * @return : File :  .<br/>
	 */
	public File transcoder(
			final Charset pCharsetSupposeEntree
			, final File pFileATranscoder
			, final Charset pCharsetSortie
				, final File pFichierSortie) {
		
		return null;
	}
	
	
	/**
	 * Getter du Charset avec lequel on suppose que le 
	 * fichier en entrée a été encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSupposeEntree : Charset : 
	 * this.charsetSupposeEntree.<br/>
	 */
	public Charset getCharsetSupposeEntree() {
		return this.charsetSupposeEntree;
	} // Fin de getCharsetSupposeEntree()._________________________________
	
	

	/**
	* Setter du Charset avec lequel on suppose que le 
	* fichier en entrée a été encodé.<br/>
	* <br/>
	*
	* @param pCharsetSupposeEntree : Charset : 
	* valeur à passer à this.charsetSupposeEntree.<br/>
	*/
	public void setCharsetSupposeEntree(
			final Charset pCharsetSupposeEntree) {
		this.charsetSupposeEntree = pCharsetSupposeEntree;
	} // Fin de setCharsetSupposeEntree(...).______________________________



	/**
	 * Getter du Fichier à transcoder en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fileATranscoder : File : this.fileATranscoder.<br/>
	 */
	public File getFileATranscoder() {
		return this.fileATranscoder;
	} // Fin de getFileATranscoder().______________________________________



	/**
	* Setter du Fichier à transcoder en charsetSortie.<br/>
	* <br/>
	*
	* @param pFileATranscoder : File : 
	* valeur à passer à this.fileATranscoder.<br/>
	*/
	public void setFileATranscoder(
			final File pFileATranscoder) {
		this.fileATranscoder = pFileATranscoder;
	} // Fin de setFileATranscoder(...).___________________________________



	/**
	 * Getter du Charset dans lequel on veut que le fichierSortie 
	 * soit encodé.<br/>
	 * <br/>
	 *
	 * @return charsetSortie : Charset : this.charsetSortie.<br/>
	 */
	public Charset getCharsetSortie() {
		return this.charsetSortie;
	} // Fin de getCharsetSortie().________________________________________



	/**
	* Setter du Charset dans lequel on veut que le fichierSortie 
	* soit encodé.<br/>
	* <br/>
	*
	* @param pCharsetSortie : Charset : 
	* valeur à passer à this.charsetSortie.<br/>
	*/
	public void setCharsetSortie(
			final Charset pCharsetSortie) {
		this.charsetSortie = pCharsetSortie;
	} // Fin de setCharsetSortie(...)._____________________________________



	/**
	 * Getter du fichier produit par le transcodeur entièrement 
	 * encodé en charsetSortie.<br/>
	 * <br/>
	 *
	 * @return fichierSortie : File : this.fichierSortie.<br/>
	 */
	public File getFichierSortie() {
		return this.fichierSortie;
	} // Fin de getFichierSortie().________________________________________



	/**
	* Setter du fichier produit par le transcodeur entièrement 
	* encodé en charsetSortie.<br/>
	* <br/>
	*
	* @param pFichierSortie : File : 
	* valeur à passer à this.fichierSortie.<br/>
	*/
	public void setFichierSortie(
			final File pFichierSortie) {
		this.fichierSortie = pFichierSortie;
	} // Fin de setFichierSortie(...)/_____________________________________
	
			
	
} // FIN DE LA CLASSE TranscodeurController.---------------------------------
