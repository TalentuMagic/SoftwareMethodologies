import 'package:flutter/material.dart';
import '../models/word.dart';
import '../services/api_service.dart';
import 'word_list_screen.dart';

class WordFormScreen extends StatefulWidget {
  final Word? word;
  const WordFormScreen({super.key, this.word});
  @override
  _WordFormScreenState createState() => _WordFormScreenState();
}

class _WordFormScreenState extends State<WordFormScreen> {
  final _form = GlobalKey<FormState>();
  final _nameCtrl = TextEditingController();
  final _defCtrl = TextEditingController();
  final api = ApiService();

  @override
  void initState() {
    super.initState();
    if (widget.word != null) {
      _nameCtrl.text = widget.word!.name;
      _defCtrl.text = widget.word!.definition;
    }
  }

  @override
  Widget build(BuildContext context) {
    final isEdit = widget.word != null;
    return Scaffold(
      appBar: AppBar(
        title: Text(isEdit ? 'Edit Word' : 'New Word'),
        actions: [
          IconButton(
            icon: const Icon(Icons.home),
            onPressed: () {
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(builder: (_) => const WordListScreen()),
                (route) => false,
              );
            },
          )
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Form(
          key: _form,
          child: Column(
            children: [
              TextFormField(
                controller: _nameCtrl,
                decoration: const InputDecoration(labelText: 'Name'),
                validator: (v) => v!.isEmpty ? 'Required' : null,
              ),
              TextFormField(
                controller: _defCtrl,
                decoration: const InputDecoration(labelText: 'Definition'),
                validator: (v) => v!.isEmpty ? 'Required' : null,
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                child: Text(isEdit ? 'Update' : 'Create'),
                onPressed: () {
                  if (_form.currentState!.validate()) {
                    final name = _nameCtrl.text;
                    final def = _defCtrl.text;
                    final call = isEdit
                        ? api.updateWord(widget.word!.id, name, def)
                        : api.createWord(name, def);
                    call.then((_) {
                      Navigator.pushAndRemoveUntil(
                        context,
                        MaterialPageRoute(
                            builder: (_) => const WordListScreen()),
                        (route) => false,
                      );
                    });
                  }
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
