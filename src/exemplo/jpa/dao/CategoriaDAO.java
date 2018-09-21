package exemplo.jpa.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import exemplo.jpa.model.Categoria;

/**
 * @author NatanPC
 *
 */
public class CategoriaDAO {
	
	// conexao com o BD
	public EntityManager getEM() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
		return factory.createEntityManager();
	}
	
	public Categoria salvar(Categoria categoria) {
		EntityManager em = getEM();
		
		try { //tratar erros
			em.getTransaction().begin(); // iniciar transa��o
			if (categoria.getId() == null){ // sem PK, ent�o � novo
				em.persist(categoria); // inser��o no BD pelo JPA
			}else {
				if (!em.contains(categoria)) { // se EntMan conhece categoria
					if (em.find(Categoria.class, categoria.getId()) == null) { 
						// n�o existe mais no BD
						throw new Exception("Erro ao atualizar a categoria");
					}
				}
				categoria = em.merge(categoria);// executa update
			}
			em.getTransaction().commit(); // finalizar transa��o
		} catch (Exception e) {
			System.err.println(e);
			em.getTransaction().rollback();
			// desfaz tudo que fez e n�o salva nada,
			// se algo der errado
		} finally {
			em.close();
		} 
		
		return categoria;
	}
	
	public void remover(Long id) {
		EntityManager em = getEM();
		Categoria c = em.find(Categoria.class, id);
		try {
			em.getTransaction().begin();
			em.remove(c); // executa o delete
			em.getTransaction().commit();
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Categoria> listarTodos(){
		EntityManager em = getEM();
		List<Categoria> categorias = null;
		
		try {
			categorias = em.createQuery("FROM Categoria c").getResultList(); //JPQL
		} catch (Exception e) {
			System.err.println(e);
		}finally {
			em.close();
		}
		
		return categorias;
	}
	
	public Categoria consultarPorId(Long id) {
		EntityManager em = getEM();
		Categoria c = null;
		
		try {
			c = em.find(Categoria.class, id); // executa o select
		} catch (Exception e) {
			System.err.println(e); 
		} finally {
			em.close();
		}
		
		return c;
		
	}
}
