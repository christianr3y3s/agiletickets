package br.com.caelum.agiletickets;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import br.com.caelum.agiletickets.models.Espetaculo;
import br.com.caelum.agiletickets.models.Estabelecimento;
import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;
import br.com.caelum.vraptor.util.jpa.EntityManagerCreator;
import br.com.caelum.vraptor.util.jpa.EntityManagerFactoryCreator;

public class PreencheBanco {

	// ALUNO: Não apague essa classe
	public static void main(String[] args) {
		EntityManagerFactoryCreator creator = new EntityManagerFactoryCreator();
		creator.create();
		EntityManagerCreator managerCreator = new EntityManagerCreator(creator.getInstance());
		managerCreator.create();
		EntityManager manager = managerCreator.getInstance();
		manager.getTransaction().begin();
		
		
		deleteTables(manager);
		
		Estabelecimento estabelecimento = getEstabelecimento();
		Espetaculo espetaculo = getEspetaculo(estabelecimento);

		persiste(manager, estabelecimento, espetaculo);

		for (int cii = 0; cii < 10; cii++) {
			Sessao sessao = createSessao(espetaculo, cii);
			persiste(manager, sessao);
		}

		manager.getTransaction().commit();
		manager.close();
	}
	
	private static void persiste(EntityManager em, Object...objects ){
		for (Object object : objects) {
			em.persist(object);
		}
	}

	private static Sessao createSessao(Espetaculo espetaculo, int cii) {
		Sessao sessao = new Sessao();
		sessao.setEspetaculo(espetaculo);
		sessao.setInicio(new DateTime().plusDays(7+cii));
		sessao.setDuracaoEmMinutos(60 * 3);
		sessao.setTotalIngressos(10);
		sessao.setIngressosReservados(10 - cii);
		return sessao;
	}

	private static Espetaculo getEspetaculo(Estabelecimento estabelecimento) {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setEstabelecimento(estabelecimento);
		espetaculo.setNome("Depeche Mode");
		espetaculo.setTipo(TipoDeEspetaculo.SHOW);
		return espetaculo;
	}

	private static Estabelecimento getEstabelecimento() {
		Estabelecimento estabelecimento = new Estabelecimento();
		estabelecimento.setNome("Casa de shows");
		estabelecimento.setEndereco("Rua dos Silveiras, 12345");
		return estabelecimento;
	}

	private static void deleteTables(EntityManager manager) {
		manager.createQuery("delete from Sessao").executeUpdate();
		manager.createQuery("delete from Espetaculo").executeUpdate();
		manager.createQuery("delete from Estabelecimento").executeUpdate();
	}
}
