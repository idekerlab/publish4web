package org.cytoscape.io.internal.task;

import java.util.Map;

import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.io.internal.preview.PreviewUtil;
import org.cytoscape.io.write.CyNetworkViewWriterFactory;
import org.cytoscape.io.write.CySessionWriterFactory;
import org.cytoscape.io.write.VizmapWriterFactory;
import org.cytoscape.util.swing.OpenBrowser;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;

public class PublishForWebTaskFactory extends AbstractTaskFactory {

	private final CySessionWriterFactory publishForWebFactory;
	private final OpenBrowser openBrowser;
	private final DialogTaskManager taskManager;

	private final VisualMappingManager vmm;
	private final PreviewUtil util;
	private final CyApplicationManager appManager;
	private final CyApplicationConfiguration appConfig;

	private VizmapWriterFactory jsonStyleWriterFactory;
	private CyNetworkViewWriterFactory cytoscapejsWriterFactory;

	public PublishForWebTaskFactory(final CySessionWriterFactory publishForWebFactory, final OpenBrowser openBrowser,
			final DialogTaskManager dialogTaskManager,
			final VisualMappingManager vmm,
			final PreviewUtil util, final CyApplicationManager appManager, final CyApplicationConfiguration appConfig) {
		super();
		
		this.publishForWebFactory = publishForWebFactory;
		this.openBrowser = openBrowser;
		this.taskManager = dialogTaskManager;

		this.vmm = vmm;
		this.util = util;
		this.appManager = appManager;
		this.appConfig = appConfig;
	}

	@Override
	public TaskIterator createTaskIterator() {
		final GeneratePreviewFileTask generatePreviewFileTask = new GeneratePreviewFileTask(
				jsonStyleWriterFactory, vmm, cytoscapejsWriterFactory, util, appManager, appConfig);
		final PreviewExportTask previewExportTask = new PreviewExportTask(openBrowser, publishForWebFactory,
				taskManager);

		return new TaskIterator(generatePreviewFileTask, previewExportTask);
	}


	@SuppressWarnings("rawtypes")
	public void registerFactory(final VizmapWriterFactory writerFactory, final Map props) {
		if (writerFactory.getClass().getName().equals("org.cytoscape.io.internal.write.json.CytoscapeJsVisualStyleWriterFactory")) {
			this.jsonStyleWriterFactory = writerFactory;
		}
	}

	@SuppressWarnings("rawtypes")
	public void unregisterFactory(final VizmapWriterFactory writerFactory, final Map props) {
	}
	
	@SuppressWarnings("rawtypes")
	public void registerViewWriterFactory(final CyNetworkViewWriterFactory writerFactory, final Map props) {
		final Object idObj = props.get("id");
		if(idObj == null) {
			return;
		}
		
		if(idObj.toString().equals("cytoscapejsNetworkWriterFactory")) {
			this.cytoscapejsWriterFactory = writerFactory;
		}
	}

	@SuppressWarnings("rawtypes")
	public void unregisterViewWriterFactory(final CyNetworkViewWriterFactory writerFactory, final Map props) {
	}
}
