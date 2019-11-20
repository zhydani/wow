package br.unitins.wow.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.unitins.wow.application.Session;
import br.unitins.wow.application.Util;
import br.unitins.wow.dao.ComposicaoDAO;
import br.unitins.wow.model.ItemComposicao;
import br.unitins.wow.model.Usuario;
import br.unitins.wow.model.Composicao;

@Named
@ViewScoped
public class PartidaComposicaoController implements Serializable {

	private static final long serialVersionUID = 780477496476930858L;

	private Composicao composicao;

	public Composicao getComposicao() {
		if (composicao == null)
			composicao = new Composicao();

		// obtendo o partidacomposicao da sessao
		List<ItemComposicao> partidacomposicao = (ArrayList<ItemComposicao>) Session.getInstance().getAttribute("partidacomposicao");

		// adicionando os itens do partidacomposicao na composicao
		if (partidacomposicao == null)
			partidacomposicao = new ArrayList<ItemComposicao>();
		composicao.setListaItemComposicao(partidacomposicao);

		return composicao;
	}

	public void remover(int idProduto) {
		// alunos vao implementar
	}

	public void finalizar() {
		Usuario usuario = (Usuario) Session.getInstance().getAttribute("usuarioLogado");
		if (usuario == null) {
			Util.addMessageWarn("Eh preciso estar logado para realizar uma composicao. Faca o Login!!");
			return;
		}
		// montar a composicao
		Composicao composicao = new Composicao();
		composicao.setData(LocalDate.now());
		composicao.setUsuario(usuario);
		List<ItemComposicao> partidacomposicao = (ArrayList<ItemComposicao>) Session.getInstance().getAttribute("partidacomposicao");
		composicao.setListaItemComposicao(partidacomposicao);
		// salvar no banco
		ComposicaoDAO dao = new ComposicaoDAO();
		try {
			dao.create(composicao);
			dao.getConnection().commit();
			Util.addMessageInfo("Composicao realizada com sucesso.");
			// limpando o partidacomposicao
			Session.getInstance().setAttribute("partidacomposicao", null);
		} catch (SQLException e) {
			dao.rollbackConnection();
			dao.closeConnection();
			Util.addMessageInfo("Erro ao finalizar a Composicao.");
			e.printStackTrace();
		}

	}

	public void setComposicao(Composicao composicao) {

		this.composicao = composicao;
	}

}
