package com.example.reviewbuddy.screens.decks

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.screens.deck.DeckDetailActivity
import com.example.reviewbuddy.screens.profile.ProfileActivity

/**
 * Controller Activity for the Study Decks management page.
 * Manages lists, search filtering, pinning, folders grouping, dynamic headers,
 * and bulk move/rename/delete selections.
 */
class DecksActivity : Activity(), DecksContract.View {

    private lateinit var presenter: DecksContract.Presenter
    private lateinit var recyclerViewDecks: RecyclerView
    private lateinit var deckAdapter: DecksListAdapter

    private lateinit var headerLayout: android.view.View
    private lateinit var layoutMultiSelectOverlay: android.view.View
    private lateinit var textSelectionCount: TextView
    private lateinit var buttonCancelSelection: android.view.View
    private lateinit var buttonOrganizeSelected: android.view.View
    private lateinit var buttonDeleteSelected: android.view.View
    private lateinit var buttonEditSelected: android.view.View
    
    private lateinit var buttonBackFromFolder: android.view.View
    private lateinit var textDecksTitle: TextView
    
    private lateinit var layoutFolderRows: LinearLayout
    private lateinit var buttonShowMoreFolders: TextView
    private lateinit var buttonCreateFolderHeader: TextView

    private var areFoldersExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decks_layout)

        presenter = DecksPresenter(this)

        recyclerViewDecks = findViewById(R.id.recyclerViewDecks)
        recyclerViewDecks.layoutManager = LinearLayoutManager(this)

        deckAdapter = DecksListAdapter(
            emptyList(),
            onDeckClick = { deck -> presenter.onDeckClicked(deck) },
            onDeckLongClick = { deck -> presenter.onDeckLongClicked(deck) }
        )
        recyclerViewDecks.adapter = deckAdapter

        // Bind all header, dynamic folders list, and bottom selection overlay layouts
        headerLayout = findViewById(R.id.headerLayout)
        layoutMultiSelectOverlay = findViewById(R.id.layoutMultiSelectOverlay)
        textSelectionCount = findViewById(R.id.textSelectionCount)
        buttonCancelSelection = findViewById(R.id.buttonCancelSelection)
        buttonOrganizeSelected = findViewById(R.id.buttonOrganizeSelected)
        buttonDeleteSelected = findViewById(R.id.buttonDeleteSelected)
        buttonEditSelected = findViewById(R.id.buttonEditSelected)
        
        buttonBackFromFolder = findViewById(R.id.buttonBackFromFolder)
        textDecksTitle = findViewById(R.id.textDecksTitle)
        
        layoutFolderRows = findViewById(R.id.layoutFolderRows)
        buttonShowMoreFolders = findViewById(R.id.buttonShowMoreFolders)
        buttonCreateFolderHeader = findViewById(R.id.buttonCreateFolderHeader)

        val edittextSearch = findViewById<EditText>(R.id.edittextSearch)
        val buttonAddDeck = findViewById<android.view.View>(R.id.buttonAddDeck)
        val navHome = findViewById<android.view.View>(R.id.navHome)
        val navDecks = findViewById<android.view.View>(R.id.navDecks)
        val navProfile = findViewById<android.view.View>(R.id.navProfile)

        // Setup textual search query listeners
        edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.onSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        buttonAddDeck.setOnClickListener { presenter.onAddDeckClicked() }
        navHome.setOnClickListener { presenter.onHomeClicked() }
        navDecks.setOnClickListener { /* Already here */ }
        navProfile.setOnClickListener { presenter.onProfileClicked() }

        // Folders header click triggers
        buttonCreateFolderHeader.setOnClickListener {
            showCreateFolderDialog()
        }

        buttonShowMoreFolders.setOnClickListener {
            areFoldersExpanded = !areFoldersExpanded
            presenter.loadDecks()
        }

        // Folder back navigation triggers
        buttonBackFromFolder.setOnClickListener {
            presenter.onFolderSelected(null)
        }
        textDecksTitle.setOnClickListener {
            if (buttonBackFromFolder.visibility == android.view.View.VISIBLE) {
                presenter.onFolderSelected(null)
            }
        }

        // Multi-select status change hooks
        deckAdapter.onSelectionChanged = { count ->
            showMultiSelectActionBar(count > 0, count)
        }

        buttonCancelSelection.setOnClickListener {
            deckAdapter.isSelectionMode = false
        }

        buttonDeleteSelected.setOnClickListener {
            val selectedIds = deckAdapter.selectedDeckIds.toSet()
            if (selectedIds.isNotEmpty()) {
                showConfirmDeleteDialog(selectedIds)
            }
        }

        buttonOrganizeSelected.setOnClickListener {
            val selectedIds = deckAdapter.selectedDeckIds.toSet()
            if (selectedIds.isNotEmpty()) {
                val all = com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.getDecks()
                val folders = all.mapNotNull { it.folder }.filter { it.isNotBlank() }.distinct().sorted()
                showFolderOrganizeDialog(selectedIds, folders)
            }
        }

        buttonEditSelected.setOnClickListener {
            val selectedIds = deckAdapter.selectedDeckIds.toSet()
            if (selectedIds.isNotEmpty()) {
                showBulkRenameDialog(selectedIds)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDecks()
    }

    /**
     * Feeds the loaded decks into the adapter.
     */
    override fun showDecks(decks: List<Deck>) {
        deckAdapter.updateDecks(decks)
    }

    /**
     * Toggles default empty states list indicators.
     */
    override fun toggleEmptyState(isEmpty: Boolean) {
        val emptyState = findViewById<TextView>(R.id.textviewEmptyState)
        emptyState?.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        recyclerViewDecks.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
    }

    /**
     * Renders a clean pop-up window to create a new flashcard Deck.
     */
    override fun showAddDeckDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_deck)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val edittextDeckTitle = dialog.findViewById<EditText>(R.id.edittextDeckTitle)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonCreate = dialog.findViewById<Button>(R.id.buttonCreate)

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonCreate.setOnClickListener {
            presenter.confirmAddDeck(edittextDeckTitle.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    /**
     * Renders options (Pin, Move to Folder, Multi-select, Delete) for a long-pressed Deck.
     */
    override fun showDeckOptionsDialog(deck: Deck) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_deck_options)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val dialogTitle = dialog.findViewById<TextView>(R.id.dialogTitle)
        val dialogSubtitle = dialog.findViewById<TextView>(R.id.dialogSubtitle)
        val textPinLabel = dialog.findViewById<TextView>(R.id.textPinLabel)
        val rowPin = dialog.findViewById<android.view.View>(R.id.rowPin)
        val rowActionSecondary = dialog.findViewById<android.view.View>(R.id.rowActionSecondary)
        val textSecondaryLabel = dialog.findViewById<TextView>(R.id.textSecondaryLabel)
        
        val rowMultiSelect = dialog.findViewById<android.view.View>(R.id.rowMultiSelect)
        
        val rowDelete = dialog.findViewById<android.view.View>(R.id.rowDelete)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        dialogTitle.text = deck.title
        dialogSubtitle.text = "Manage options for this deck"

        // Handle Pinning Click
        textPinLabel.text = if (deck.isPinned) "Unpin Deck" else "Pin Deck"
        rowPin.setOnClickListener {
            presenter.togglePinDeck(deck)
            dialog.dismiss()
        }

        // Handle Folders Migration Row
        textSecondaryLabel.text = "Move to Folder"
        rowActionSecondary.setOnClickListener {
            dialog.dismiss()
            val all = com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.getDecks()
            val folders = all.mapNotNull { it.folder }.filter { it.isNotBlank() }.distinct().sorted()
            showFolderOrganizeDialog(setOf(deck.id), folders)
        }

        // Trigger Contextual Multi-Selection overlay
        rowMultiSelect.setOnClickListener {
            dialog.dismiss()
            deckAdapter.isSelectionMode = true
            deckAdapter.selectedDeckIds.add(deck.id)
            deckAdapter.notifyDataSetChanged()
            deckAdapter.onSelectionChanged?.invoke(deckAdapter.selectedDeckIds.size)
        }

        // Trigger Delete confirmation action popup
        rowDelete.setOnClickListener {
            dialog.dismiss()
            showConfirmDeleteDialog(setOf(deck.id))
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Renders a highly-cohesive, custom confirmation dialog before executing deck deletions.
     */
    private fun showConfirmDeleteDialog(deckIds: Set<String>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_confirm_action)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val textConfirmTitle = dialog.findViewById<TextView>(R.id.textConfirmTitle)
        val textConfirmMessage = dialog.findViewById<TextView>(R.id.textConfirmMessage)
        val buttonConfirm = dialog.findViewById<Button>(R.id.buttonConfirm)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        if (deckIds.size > 1) {
            textConfirmTitle.text = "Delete ${deckIds.size} Decks?"
            textConfirmMessage.text = "Are you sure you want to permanently delete these ${deckIds.size} decks? This action cannot be undone."
        } else {
            val deck = com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.getDeckById(deckIds.first())
            textConfirmTitle.text = "Delete '${deck?.title ?: "Deck"}'?"
            textConfirmMessage.text = "Are you sure you want to permanently delete this deck? This action cannot be undone."
        }

        buttonConfirm.setOnClickListener {
            presenter.deleteDecks(deckIds)
            deckAdapter.isSelectionMode = false
            dialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Renders a dynamic folders list to bulk move selected Decks.
     */
    override fun showFolderOrganizeDialog(deckIds: Set<String>, folders: List<String>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_folder_action)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val edittextFolderName = dialog.findViewById<EditText>(R.id.edittextFolderName)
        val layoutFolderList = dialog.findViewById<LinearLayout>(R.id.layoutFolderList)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSave)

        layoutFolderList.removeAllViews()
        
        // Add unassigned row to exit a folder
        val noneRow = TextView(this).apply {
            text = "📁 [None / Remove from Folder]"
            textSize = 14f
            setTextColor(resources.getColor(R.color.text_primary))
            setPadding(32, 24, 32, 24)
            setBackgroundResource(R.drawable.bg_card)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 12
            }
            setOnClickListener {
                presenter.moveDecksToFolder(deckIds, null)
                deckAdapter.isSelectionMode = false
                dialog.dismiss()
            }
        }
        layoutFolderList.addView(noneRow)

        // Populate active existing folder selectors
        folders.forEach { folderName ->
            val row = TextView(this).apply {
                text = "📁 $folderName"
                textSize = 14f
                setTextColor(resources.getColor(R.color.text_primary))
                setPadding(32, 24, 32, 24)
                setBackgroundResource(R.drawable.bg_card)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 12
                }
                setOnClickListener {
                    presenter.moveDecksToFolder(deckIds, folderName)
                    deckAdapter.isSelectionMode = false
                    dialog.dismiss()
                }
            }
            layoutFolderList.addView(row)
        }

        buttonSave.setOnClickListener {
            val newFolder = edittextFolderName.text.toString()
            if (newFolder.isNotBlank()) {
                presenter.moveDecksToFolder(deckIds, newFolder)
                deckAdapter.isSelectionMode = false
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a folder name or click an existing one", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    /**
     * Renders folder creation pop-up interfaces.
     */
    private fun showCreateFolderDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_folder_action)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val edittextFolderName = dialog.findViewById<EditText>(R.id.edittextFolderName)
        val header = dialog.findViewById<TextView>(R.id.textExistingFoldersHeader)
        val list = dialog.findViewById<LinearLayout>(R.id.layoutFolderList)
        val title = dialog.findViewById<TextView>(R.id.dialogFolderTitle)
        
        title.text = "Create Folder"
        header.visibility = android.view.View.GONE
        list.visibility = android.view.View.GONE

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSave)

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonSave.setOnClickListener {
            val folderName = edittextFolderName.text.toString()
            if (folderName.isNotBlank()) {
                presenter.onFolderSelected(folderName.trim())
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Folder name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    /**
     * Renders a sequential renamer dialog for renaming multiple decks concurrently.
     */
    private fun showBulkRenameDialog(deckIds: Set<String>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_deck)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val titleView = dialog.findViewById<TextView>(R.id.dialogTitle)
        val subtitleView = dialog.findViewById<TextView>(R.id.dialogSubtitle)
        val edittextDeckTitle = dialog.findViewById<EditText>(R.id.edittextDeckTitle)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonCreate = dialog.findViewById<Button>(R.id.buttonCreate)

        titleView.text = "Bulk Rename Decks"
        subtitleView.text = "Rename ${deckIds.size} selected decks"
        edittextDeckTitle.hint = "Enter base name"
        buttonCreate.text = "Rename"

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonCreate.setOnClickListener {
            val baseName = edittextDeckTitle.text.toString().trim()
            if (baseName.isNotBlank()) {
                var count = 1
                deckIds.forEach { id ->
                    val deck = com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.getDeckById(id)
                    if (deck != null) {
                        val newTitle = if (deckIds.size > 1) "$baseName $count" else baseName
                        com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.updateDeckTitle(id, newTitle)
                        count++
                    }
                }
                deckAdapter.isSelectionMode = false
                presenter.loadDecks()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Base name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    /**
     * Dynamic folder overview layout filters. Automatically handles search overlays,
     * folder back buttons navigation headers, 3-folder layout limits, and vertical Card-rows.
     */
    override fun showFolderTabs(folders: List<String>, activeFolder: String?) {
        layoutFolderRows.removeAllViews()

        val edittextSearch = findViewById<EditText>(R.id.edittextSearch)
        val isSearching = edittextSearch?.text?.toString()?.isNotBlank() ?: false

        val foldersHeader = findViewById<android.view.View>(R.id.layoutFoldersHeader)
        val separator = findViewById<android.view.View>(R.id.viewSeparator)
        val decksHeader = findViewById<TextView>(R.id.layoutDecksHeader)

        if (isSearching) {
            foldersHeader.visibility = android.view.View.GONE
            layoutFolderRows.visibility = android.view.View.GONE
            buttonShowMoreFolders.visibility = android.view.View.GONE
            separator.visibility = android.view.View.GONE
            decksHeader.text = "Search Results"
            
            buttonBackFromFolder.visibility = android.view.View.GONE
            textDecksTitle.text = "My Decks"
            return
        }

        // Handle Active Folder isolation: hide other folders list and activate simple chevron navigation headers
        if (activeFolder != null) {
            foldersHeader.visibility = android.view.View.GONE
            layoutFolderRows.visibility = android.view.View.GONE
            buttonShowMoreFolders.visibility = android.view.View.GONE
            separator.visibility = android.view.View.GONE
            decksHeader.text = "Decks in '$activeFolder'"

            buttonBackFromFolder.visibility = android.view.View.VISIBLE
            textDecksTitle.text = activeFolder
            return
        }

        // Root vertical listing overview
        foldersHeader.visibility = android.view.View.VISIBLE
        layoutFolderRows.visibility = android.view.View.VISIBLE
        separator.visibility = android.view.View.VISIBLE
        decksHeader.text = "Decks"

        buttonBackFromFolder.visibility = android.view.View.GONE
        textDecksTitle.text = "My Decks"

        // Toggle visibility limits for folders (3 initially)
        val shownFolders = if (folders.size > 3) {
            buttonShowMoreFolders.visibility = android.view.View.VISIBLE
            if (areFoldersExpanded) {
                buttonShowMoreFolders.text = "Show Less ▵"
                folders
            } else {
                buttonShowMoreFolders.text = "Show More ▽ (${folders.size - 3} more)"
                folders.take(3)
            }
        } else {
            buttonShowMoreFolders.visibility = android.view.View.GONE
            folders
        }

        // Render card views vertically
        shownFolders.forEach { folderName ->
            val folderDecks = com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.getDecks().filter { it.folder == folderName }
            val deckCount = folderDecks.size
            val isActive = folderName == activeFolder

            val folderRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding(48, 36, 48, 36)
                setBackgroundResource(R.drawable.bg_card)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 16
                }

                if (isActive) {
                    setBackgroundColor(android.graphics.Color.parseColor("#EEF2FF"))
                }

                val iconView = TextView(context).apply {
                    text = "📁 "
                    textSize = 18f
                }
                addView(iconView)

                val detailsLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    ).apply {
                        marginStart = 24
                    }

                    val titleView = TextView(context).apply {
                        text = folderName
                        textSize = 15f
                        setTypeface(null, android.graphics.Typeface.BOLD)
                        setTextColor(resources.getColor(R.color.text_primary))
                    }
                    val countView = TextView(context).apply {
                        text = "$deckCount Decks"
                        textSize = 12f
                        setTextColor(resources.getColor(R.color.text_secondary))
                    }
                    addView(titleView)
                    addView(countView)
                }
                addView(detailsLayout)

                if (isActive) {
                    val indicator = TextView(context).apply {
                        text = "Active ✓"
                        textSize = 12f
                        setTextColor(android.graphics.Color.parseColor("#4F46E5"))
                        setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                    addView(indicator)
                }

                setOnClickListener {
                    if (isActive) {
                        presenter.onFolderSelected(null)
                    } else {
                        presenter.onFolderSelected(folderName)
                    }
                }
            }
            layoutFolderRows.addView(folderRow)
        }
    }

    /**
     * Animates or toggles display visibility of the bottom multi-select actions bar overlay.
     */
    override fun showMultiSelectActionBar(visible: Boolean, count: Int) {
        if (visible) {
            layoutMultiSelectOverlay.visibility = android.view.View.VISIBLE
            textSelectionCount.text = "$count Selected"
        } else {
            layoutMultiSelectOverlay.visibility = android.view.View.GONE
        }
    }

    override fun showDeckDetails(deck: Deck) {
        val intent = Intent(this, DeckDetailActivity::class.java)
        intent.putExtra("DECK_ID", deck.id)
        intent.putExtra("DECK_TITLE", deck.title)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        finish()
    }

    override fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
