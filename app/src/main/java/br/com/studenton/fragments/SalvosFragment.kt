package br.com.studenton.fragments

import android.app.AlertDialog
import android.net.DnsResolver
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.AdapterSalvoResponse
import br.com.studenton.databinding.FragmentSalvosBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.Salvo
import br.com.studenton.repository.Rest
import br.com.studenton.services.SalvarService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale


class SalvosFragment : Fragment() {

    private lateinit var listaSalvos : MutableList<Publicacao>
    private lateinit var recyclerViewSalvos : RecyclerView
    private lateinit var binding: FragmentSalvosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalvosBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuario = arguments?.getInt("id")

        recyclerViewSalvos = binding.recyclerViewSalvos

        recyclerViewSalvos.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        recyclerViewSalvos.setHasFixedSize(true)

        binding.shimmerFrameLayoutSalvos.startShimmerAnimation()
        binding.shimmerFrameLayoutSalvos.visibility = View.VISIBLE
        binding.recyclerViewSalvos.visibility = View.GONE

        Rest.getInstance<SalvarService>().getFavoritosByUsuario(usuario!!)
            .enqueue(object : Callback<MutableList<Publicacao>>{

                override fun onResponse(
                    call: Call<MutableList<Publicacao>>,
                    response: Response<MutableList<Publicacao>>
                ) {

                    listaSalvos = (response.body()!!)

                    recyclerViewSalvos.adapter = AdapterSalvoResponse(listaSalvos) {id ->
                        carregarPublicacao(id)
                    }

                    binding.shimmerFrameLayoutSalvos.stopShimmerAnimation()
                    binding.shimmerFrameLayoutSalvos.visibility = View.GONE
                    binding.recyclerViewSalvos.visibility = View.VISIBLE

                    val helper = androidx.recyclerview.widget.ItemTouchHelper(
                        ItemTouchHelper(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT))

                    helper.attachToRecyclerView(recyclerViewSalvos)

                    AdapterSalvoResponse(listaSalvos){id -> carregarPublicacao(id)}.onItemLongClick = {
                        Log.i("Teste", "onItemLongClick")
                    }

                    }

                override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                    Log.i("Erro ao chamar publicações", t.stackTraceToString())
                }

            })

    }

    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int): androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
        dragDirs, swipeDirs
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from: Int = viewHolder.adapterPosition
            val to:Int = target.adapterPosition

            Collections.swap(listaSalvos, from, to)
            recyclerViewSalvos.adapter?.notifyItemMoved(from, to)
            return true;
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            val usuario = arguments?.getInt("id")
            val idPublicacao = arguments?.getInt("idPublicacao")

            listaSalvos.removeAt(viewHolder.adapterPosition)
            recyclerViewSalvos.adapter?.notifyItemRemoved(viewHolder.adapterPosition)

            apagarSalvo(usuario!!, idPublicacao!!)
        }
    }

    private fun carregarPublicacao(idPublicacao: Int){

        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val visualizarSalvo = VisualizarSalvoFragment()

        visualizarSalvo.arguments = bundleOf("id" to idPublicacao)

        transaction.replace(R.id.fragments_container, visualizarSalvo)

        transaction.commit()
    }

    private fun apagarSalvo(idUsuario: Int, idPublicacao: Int){

        Rest.getInstance<SalvarService>().deletarFavorito(idUsuario, idPublicacao).enqueue(
            object: Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    val alerta = AlertDialog.Builder(context!!);

                    alerta.setTitle(R.string.deletar_salvo_title)

                    alerta.setMessage(R.string.deletar_salvo_text)

                    alerta.create().show()

                    Log.i("Resposta:", response.toString())
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

    }
}

