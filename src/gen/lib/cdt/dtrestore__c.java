/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program:
 * 
 *************************************************************************
 * Copyright (c) 2011 AT&T Intellectual Property 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: See CVS logs. Details at http://www.graphviz.org/
 *************************************************************************
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package gen.lib.cdt;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.Dtsearch_f;
import h.ST_dt_s;
import h.ST_dtlink_s;

public class dtrestore__c {
//1 9k44uhd5foylaeoekf3llonjq
// extern Dtmethod_t* 	Dtset


//1 1ahfywsmzcpcig2oxm7pt9ihj
// extern Dtmethod_t* 	Dtbag


//1 anhghfj3k7dmkudy2n7rvt31v
// extern Dtmethod_t* 	Dtoset


//1 5l6oj1ux946zjwvir94ykejbc
// extern Dtmethod_t* 	Dtobag


//1 2wtf222ak6cui8cfjnw6w377z
// extern Dtmethod_t*	Dtlist


//1 d1s1s6ibtcsmst88e3057u9r7
// extern Dtmethod_t*	Dtstack


//1 axa7mflo824p6fspjn1rdk0mt
// extern Dtmethod_t*	Dtqueue


//1 ega812utobm4xx9oa9w9ayij6
// extern Dtmethod_t*	Dtdeque


//1 cyfr996ur43045jv1tjbelzmj
// extern Dtmethod_t*	Dtorder


//1 wlofoiftbjgrrabzb2brkycg
// extern Dtmethod_t*	Dttree


//1 12bds94t7voj7ulwpcvgf6agr
// extern Dtmethod_t*	Dthash


//1 9lqknzty480cy7zsubmabkk8h
// extern Dtmethod_t	_Dttree


//1 bvn6zkbcp8vjdhkccqo1xrkrb
// extern Dtmethod_t	_Dthash


//1 9lidhtd6nsmmv3e7vjv9e10gw
// extern Dtmethod_t	_Dtlist


//1 34ujfamjxo7xn89u90oh2k6f8
// extern Dtmethod_t	_Dtqueue


//1 3jy4aceckzkdv950h89p4wjc8
// extern Dtmethod_t	_Dtstack




//3 8dfut8799tvg4tjvn2yz48d7h
// int dtrestore(register Dt_t* dt, register Dtlink_t* list)      
public static int dtrestore(ST_dt_s dt, ST_dtlink_s list) {
ENTERING("8dfut8799tvg4tjvn2yz48d7h","dtrestore");
try {
	ST_dtlink_s	t;
	//Dtlink_t	**s, **ends;
	int		type;
	Dtsearch_f	searchf = (Dtsearch_f) dt.meth.searchf;
	type = dt.data.type&010000;
	if(N(list)) /* restoring a flattened dictionary */
	{	if(N(type))
			return -1;
		list = (ST_dtlink_s) dt.data.here;
	}
	else	/* restoring an extracted list of elements */
	{	if(dt.data.size != 0)
			return -1;
		type = 0;
	}
	dt.data.setInt("type", dt.data.type & ~010000);
	if((dt.data.type&(0000001|0000002))!=0)
	{	//dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("4xawc48hce5sov89n8h4j7xw0"); // 		if(type) /* restoring a flattened dictionary */
UNSUPPORTED("3yrjmgus9a9415ocrrtj1e733"); // 		{	for(ends = (s = dt->data->hh._htab) + dt->data->ntab; s < ends; ++s)
UNSUPPORTED("7s6h8lg0p1wwil85e1p2msogy"); // 			{	if((t = *s) )
UNSUPPORTED("2j3e40zcb5znwl73hbk12t6va"); // 				{	*s = list;
UNSUPPORTED("ef469bao0q1pw0jwv7pmoly31"); // 					list = t->right;
UNSUPPORTED("29gliugc08l5pj89nfqoctwqi"); // 					t->right = ((Dtlink_t*)0);
UNSUPPORTED("cysnuxd51taci3hbg5lifz8ce"); // 				}
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("dri3t8bbbtcxexw436q0kqnxd"); // 		else	/* restoring an extracted list of elements */
UNSUPPORTED("efiynrcqevfta9kp0bfbujmn5"); // 		{	dt->data->size = 0;
UNSUPPORTED("afqkqg6k8jxzgjyj7tb6kw1fc"); // 			while(list)
UNSUPPORTED("4rgxhjb5r159n4fh7vilshms9"); // 			{	t = list->right;
UNSUPPORTED("6fq6k0xv0ofue3nmsm3tcbmxc"); // 				(*searchf)(dt,(void*)list,0000040);
UNSUPPORTED("8dsqgg8k8zwg5ae8ltd5xs9yc"); // 				list = t;
UNSUPPORTED("3to5h0rvqxdeqs38mhv47mm3o"); // 			}
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
	}
	else
	{	if((dt.data.type&(0000004|0000010))!=0)
			dt.data.here = list;
		else /*if(dt->data->type&(DT_LIST|DT_STACK|DT_QUEUE))*/
{UNSUPPORTED("79wu0m7edfdq02msgoqkzb32"); // 		{	dt->data->here = ((Dtlink_t*)0);
UNSUPPORTED("c8q10nh3f6o4rjxtacmbgjxqj"); // 			dt->data->hh._head = list;
		}
		if(N(type))
			dt.data.setInt("size", -1);
	}
	return 0;
} finally {
LEAVING("8dfut8799tvg4tjvn2yz48d7h","dtrestore");
}
}


}
