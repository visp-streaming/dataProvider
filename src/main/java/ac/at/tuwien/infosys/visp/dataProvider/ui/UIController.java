package ac.at.tuwien.infosys.visp.dataProvider.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class UIController {


	@RequestMapping("/about")
	public String index() {
		return "about";
	}


	@RequestMapping("/topology")
	public String topology() {
		return "topology";
	}


}
