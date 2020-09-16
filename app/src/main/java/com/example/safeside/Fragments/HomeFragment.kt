package com.example.safeside.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safeside.SubActivities.ArticleAdapter
import com.example.safeside.DataModel.Article
import com.example.safeside.R
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val views = inflater.inflate(R.layout.fragment_home, container, false)

        val list = generateList()

        val recycler = views.findViewById(R.id.recyclerView) as RecyclerView

        recycler.adapter = ArticleAdapter(list)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.setHasFixedSize(true)

        return views
    }

    private fun generateList() : List<Article> {
        val item = Article("akshay@gmail.com",
            "Akshay Khanna",
            "We can not only share our incident or accident but also we can also share something good about a place. Let's make this world a happy and safe place to live. Spread happiness and awareness as well.\n\n#SaveLifes #AwesomeApp #BeAHelpingHand",
            "7",
            "3", "14:20", "Today")
        val item2 = Article("akshay@gmail.com",
            "Ashu",
            "Source: THE ECONOMIC TIMES\n\nBengaluru not far behind Delhi in crime against women. Delhi’s infamy for violent crime against women is well documented. It is surprising to note that Bengaluru is nearly as unsafe. CRIMES INCLUDED: Murder with rape/gang rape (100%), Dowry deaths (100%), Abetment to suicide of women (100%), Miscarriage (50%), Acid attack (50%), Attempt to acid attack (40%)",
            "4",
            "1", "17:56", "Yesterday")

        val item3 = Article(
            "ashu",
            "Vaibhav Joshi",
            "CHANDIGARH: The resident welfare association (RWA) of Manimajra has demanded speed breakers and 'no parking' signages at key accident-prone spots.\n" +
                "A delegation of the RWA, led by colonel (retd) Gursewak Singh, met director general of police (DGP) Sanjay Beniwal in this regard recently.\n" +
                "The delegation also wrote a letter to the advisor of the UT administrator Manoj Parida and MC commissioner K K Yadav, demanding immediate action on the issue lest face a major mishap before acting.\n",
            "11",
            "6", "19:34", "18.08.2020")

         val list = ArrayList<Article>()
        list.add(item)
        list.add(item2)
        list.add(item3)

        return list
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}